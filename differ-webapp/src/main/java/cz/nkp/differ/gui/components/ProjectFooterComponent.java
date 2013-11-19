package cz.nkp.differ.gui.components;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates the footer of the main application window, adding banners/logos/text as necessary.
 * @author Thomas Truax
 */
public class ProjectFooterComponent extends CustomComponent {
    
    public ProjectFooterComponent() {
        Panel footerPanel = new Panel();
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("100%");
        Layout footer = createFooterLayout();
        layout.addComponent(footer);
        layout.setComponentAlignment(footer, Alignment.MIDDLE_CENTER);
        footerPanel.setWidth("100%");
        footerPanel.addComponent(layout);
        this.setCompositionRoot(footerPanel);
    }
    
    private Layout createFooterLayout() {
        List<Link> links = new ArrayList<Link>();
        
        Link mk = new Link("", new ExternalResource("http://www.mkcr.cz/"));
        mk.setIcon(new ThemeResource("img/LOGO_Ministerstvo_Kultury.png"));
        links.add(mk);
        
        Link gsoc = new Link("", new ExternalResource("https://developers.google.com/open-source/soc/"));
        gsoc.setIcon(new ThemeResource("img/LOGO_gsoc_2013.png"));
        links.add(gsoc);
        
        Link nkp = new Link("", new ExternalResource("http://www.nkp.cz/"));
        nkp.setIcon(new ThemeResource("img/LOGO_nkp_en.png"));
        links.add(nkp);
        
        GridLayout layout = new GridLayout(links.size(), 1);
        layout.addStyleName("v-footer-logos");
        for (Link link : links) {
            layout.addComponent(link);
        }
        return layout;
    }
}
