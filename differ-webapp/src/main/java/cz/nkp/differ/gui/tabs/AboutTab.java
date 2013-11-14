package cz.nkp.differ.gui.tabs;

import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import cz.nkp.differ.gui.components.ExternalHTMLComponent;

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
