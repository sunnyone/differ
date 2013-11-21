package cz.nkp.differ.gui.windows;

import com.vaadin.ui.Layout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.gui.components.ProjectFooterComponent;
import cz.nkp.differ.gui.components.ProjectHeaderPanel;
import cz.nkp.differ.gui.components.UserFilesWidget;
import cz.nkp.differ.gui.tabs.DifferProgramTab;
import cz.nkp.differ.gui.tabs.ResultManagerTab;
import cz.nkp.differ.gui.tabs.TabLoader;
import cz.nkp.differ.listener.LoginListener;
import cz.nkp.differ.model.User;
import java.io.IOException;
import java.util.List;

/**
 * 
 * @author Joshua Mabrey
 * @author Thomas Truax
 * 
 * Sept. 2013
 */
@SuppressWarnings("serial")
public class MainDifferWindow extends Window implements LoginListener {
    
    private static final int POSITION_OF_RESULT_IN_TABS = 2;

    private List<UserFilesWidget> userFilesWidgets;
    private TabSheet menuTabs;
    private Tab resultsTab = null;
    
    public MainDifferWindow() {
	super("The Image Data Validator - DIFFER");//Sets the title of the application

	menuTabs = new TabSheet();

	/*
	 * Adding the dynamic content tabs
	 */
        DifferProgramTab loginContext = new DifferProgramTab(this);
	MainDifferWindow.createDynamicContentTab(loginContext, "DIFFER", menuTabs);
        
        ResultManagerTab examples = new ResultManagerTab(this, true);
        MainDifferWindow.createDynamicContentTab(examples, "Examples", menuTabs);
        
        if (DifferApplication.getCurrentApplication().getLoggedUser() != null) {
            ResultManagerTab results = new ResultManagerTab(this, false);
            resultsTab = MainDifferWindow.createDynamicContentTab(results, "Results", menuTabs);
        }
        
	MainDifferWindow.createStaticContentTab("doc_tab", "Documents", menuTabs);
	MainDifferWindow.createStaticContentTab("about_tab", "About", menuTabs);
        MainDifferWindow.createStaticContentTab("traffic_tab", "Traffic", menuTabs);

	/*
	 * Add the actual completed UI components to the root
	 */
	addComponent(new ProjectHeaderPanel(this, loginContext));//Component that represents the top-page header
	addComponent(menuTabs);//The application view tabs
        addComponent(new ProjectFooterComponent());
    }
    
    public void init() {
        DifferApplication.getCurrentApplication().addLoginListener(this);
    }

    private static Tab createStaticContentTab(String source, String caption, TabSheet parent) {
	try {
	    VerticalLayout tab = new TabLoader(source);
	    tab.setMargin(true);
	    tab.setCaption(caption);
	    return parent.addTab(tab);
	} catch (IOException e) {
	    e.printStackTrace();
	}
        return null;
    }

    private static Tab createDynamicContentTab(Layout source, String caption, TabSheet parent) {
	source.setCaption(caption);
	source.setMargin(true);
	return parent.addTab(source);
    }
    
    private static Tab createDynamicContentTab(Layout source, String caption, TabSheet parent, int position) {
	source.setCaption(caption);
	source.setMargin(true);
	return parent.addTab(source, position);
    }

    public List<UserFilesWidget> getUserFilesWidgets() {
	return userFilesWidgets;
    }

    public void setUserFilesWidgets(List<UserFilesWidget> userFilesWidgets) {
	this.userFilesWidgets = userFilesWidgets;
    }

    @Override
    public void onLogin(User user) {
        if (resultsTab == null) {
            ResultManagerTab results = new ResultManagerTab(this, false);
            resultsTab = MainDifferWindow.createDynamicContentTab(results, "Results", menuTabs, POSITION_OF_RESULT_IN_TABS);
        }
    }

    @Override
    public void onLogout(User user) {
        menuTabs.removeTab(resultsTab);
        resultsTab = null;
    }
    
}
