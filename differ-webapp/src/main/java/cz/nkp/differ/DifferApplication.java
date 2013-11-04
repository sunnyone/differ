package cz.nkp.differ;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Window;
import cz.nkp.differ.compare.io.ImageThumbnailProvider;
import cz.nkp.differ.compare.metadata.MetadataGroups;
import cz.nkp.differ.configuration.Configuration;
import cz.nkp.differ.configuration.GoogleAnalyticsConfiguration;
import cz.nkp.differ.gui.windows.MainDifferWindow;
import cz.nkp.differ.io.ImageManager;
import cz.nkp.differ.io.ResultManager;
import cz.nkp.differ.model.User;
import cz.nkp.differ.profile.EditableJP2ProfileProvider;
import cz.nkp.differ.user.UserManager;
import cz.nkp.differ.util.TemporaryFilesCleaner;
import eu.livotov.tpt.TPTApplication;
import java.security.Security;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletContext;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.vaadin.googleanalytics.tracking.GoogleAnalyticsTracker;

/**
 * The main Application instance, responsible for setting global settings, such as locale, theme, and the root window for the GUI.
 * This class also allows any code to fetch the current Application instance in a thread-safe way.
 * @author Joshua Mabrey
 * Mar 30, 2012
 */
@SuppressWarnings("serial")
public class DifferApplication extends TPTApplication {
    
    private static final String DIFFER_THEME_NAME = "differ";
    private static Logger LOGGER = Logger.getLogger(DifferApplication.class);

    /* static class members */
    protected static UserManager userManager = null;
    protected static ImageManager imageManager = null;
    protected static ResultManager resultManager = null;
    protected static ImageThumbnailProvider imageThumbnailProvider = null;
    protected static ApplicationContext applicationContext = null;
    protected static MainDifferWindow mainDifferWindow = null;
    protected static EditableJP2ProfileProvider editableJP2ProfileProvider = null;
    protected static MetadataGroups metadataGroups = null;
    protected static Configuration configuration = null;
    protected static GoogleAnalyticsTracker gaTracker = null;
    
    protected static final TemporaryFilesCleaner temporaryFilesCleaner = new TemporaryFilesCleaner();
    static {
        temporaryFilesCleaner.init();
    }

    /*
     * We dont need an X server running on a display to do graphics operations. May be slower on some machines.
     * TODO: examine a switching option for this setting
     */
    static {
	System.setProperty("java.awt.headless", "true");
    }

    /**
     * Called by the server to run the application and begin the session
     * FIXME: move same parts to firstApplicationStartup()
     */
    @Override
    public void applicationInit() {
	//Setup Apache Log4j Configuration
	BasicConfigurator.configure();

	//BouncyCastle Setup
	Security.addProvider(new BouncyCastleProvider());

	setTheme(DIFFER_THEME_NAME);//Set to custom differ theme
	LOGGER.trace("Loaded Vaadin theme: " + DIFFER_THEME_NAME);

	//Get Application Context
	WebApplicationContext context = (WebApplicationContext) getContext();

	//Set Context Locale to Browser Locale
	Locale locale = context.getBrowser().getLocale();
	setLocale(locale);
	LOGGER.trace("Session Locale: " + locale.getDisplayName());

	ServletContext servletContext = ((WebApplicationContext) this.getContext()).getHttpSession().getServletContext();
        applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	userManager = (UserManager) applicationContext.getBean("userManager");
	imageManager = (ImageManager) applicationContext.getBean("imageManager");
	resultManager = (ResultManager) applicationContext.getBean("resultManager");
        imageThumbnailProvider = (ImageThumbnailProvider) applicationContext.getBean("imageThumbnailProvider");
        editableJP2ProfileProvider = (EditableJP2ProfileProvider) applicationContext.getBean("editableJP2ProfileProvider");
        metadataGroups = (MetadataGroups) applicationContext.getBean("metadataGroups");
        
        configuration = (Configuration) applicationContext.getBean("differConfiguration");
        //FIXME: hardcoded
        /*
        String resultsPath = "/tmp/differ/" + userManager.getLoggedInUser() + "/results";
        new File(resultsPath).mkdirs(); //create results folder if doesn't exist
        resultManager.setDirectory(resultsPath);
        */
	mainDifferWindow = new MainDifferWindow();
	mainDifferWindow.setSizeUndefined();
	setMainWindow(mainDifferWindow);
        
        if (configuration.getGoogleAnalyticsConfiguration() != null
                && configuration.getGoogleAnalyticsConfiguration().isEnabled()) {
            GoogleAnalyticsConfiguration gaConf = configuration.getGoogleAnalyticsConfiguration();
            gaTracker = new GoogleAnalyticsTracker(gaConf.getTrackerId(), gaConf.getDomainName());
            mainDifferWindow.addComponent(gaTracker);
            gaTracker.trackPageview("/");
        }
       
    }

    @Override
    public void firstApplicationStartup() {
    }

    @Override
    public Window getWindow(String name) {
	Window window = super.getWindow(name);
	if (window == null) {
	    window = new MainDifferWindow();
	    window.setSizeUndefined();
	    window.setName(name);
	    addWindow(window);
	    window.open(new ExternalResource(window.getURL()));
	}
	return window;
    }

    public User getLoggedUser() {
	return (User) super.getUser();
    }

    public void setLoggedUser(User loggedUser) {
	super.setUser(loggedUser);
    }

    public static ImageThumbnailProvider getImageThumbnailProvider() {
        return imageThumbnailProvider;
    }
    
    public static ImageManager getImageManager() {
	return imageManager;
    }

    public static UserManager getUserManager() {
	return userManager;
    }

    public static ResultManager getResultManager() {
	return resultManager;
    }
    
    public static GoogleAnalyticsTracker getGATracker() {
        return gaTracker;
    }

    public static EditableJP2ProfileProvider getEditableJP2ProfileProvider() {
        return editableJP2ProfileProvider;
    }

    public static MetadataGroups getMetadataGroups() {
        return metadataGroups;
    }
    
    public static Configuration getConfiguration() {
        return configuration;
    }

    public static TemporaryFilesCleaner getTemporaryFilesCleaner() {
        return temporaryFilesCleaner;
    }

    public static ApplicationContext getApplicationContext() {
	return applicationContext;
    }

    public static DifferApplication getCurrentApplication() {
        return (DifferApplication) TPTApplication.getCurrentApplication();
    }

    public static Window getMainApplicationWindow() {
        return mainDifferWindow;
    }

    public float getScreenWidth() {
	return getMainWindow().getWidth();
    }

}
