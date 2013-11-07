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
 * @author Jan Stavel
 */
public class AboutTab extends VerticalLayout {
    
    public AboutTab(Window parent) {
        Panel mainpanel = new Panel();
        ExternalHTMLComponent extres = new ExternalHTMLComponent(parent, "https://differ.readthedocs.org/en/latest/introduction/");

        Component cmp = extres.getComponent();
        cmp.setStyleName("v-embedded-docs");
        mainpanel.addComponent(cmp);
        addComponent(mainpanel);
        
        setSizeFull();
    }
}
