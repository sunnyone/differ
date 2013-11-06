package cz.nkp.differ.gui.tabs;

import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Window.ResizeListener;
import com.vaadin.ui.themes.Runo;
import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.gui.components.ExternalHTMLComponent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thomas Truax
 * @author Jan Stavel
 */
public class HelpTab extends VerticalLayout {
    
    public HelpTab(Window parent) {
        Panel mainpanel = new Panel();
        // CustomLayout custom = new CustomLayout("webapp");
        // mainpanel.setContent(custom);
        // addComponent(mainpanel);
        
        // Embedded logo = new Embedded(null, new ThemeResource("_build/html/_static/differ-logo.png"));
        // Embedded content = new Embedded(null, new ThemeResource("_build/html/webapp.html"));
        // mainpanel.addComponent(logo);
        // mainpanel.addComponent(content);
        // addComponent(mainpanel);
        
        //setSizeFull();
        
        //CustomLayout custom = new CustomLayout("help_tab");
        // // custom.setWidth("100%");

        // /*
        //  * TODO: add http address of documentation in line below
        //  */
         ExternalHTMLComponent extres = new ExternalHTMLComponent(parent, "https://differ.readthedocs.org/en/latest/webapp/");

        Component cmp = extres.getComponent();
        cmp.setStyleName("v-embedded-docs");
        mainpanel.addComponent(cmp);
        addComponent(mainpanel);
        
        setSizeFull();
    }
}
