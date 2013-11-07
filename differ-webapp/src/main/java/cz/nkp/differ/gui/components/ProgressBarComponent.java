package cz.nkp.differ.gui.components;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import cz.nkp.differ.compare.io.CompareComponent;
import cz.nkp.differ.model.Image;
import org.apache.log4j.Logger;

public class ProgressBarComponent extends CustomComponent {

    private static final long serialVersionUID = -5172306282663506101L;
    private Logger LOGGER = Logger.getLogger(ProgressBarComponent.class);

    public ProgressBarComponent(Object lock, CompareComponent compareComponent, Image[] images) {
        super();
        if (images == null) {
            throw new NullPointerException("images");
        }
        this.setCompositionRoot(createPluginCompareComponent(lock, compareComponent, images));

    }

    private Layout createPluginCompareComponent(Object lock, CompareComponent compareComponent, Image[] images) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.addComponent(new ProgressBarPanel(lock, compareComponent, images));
        return layout;
    }
}
