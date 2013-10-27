package cz.nkp.differ.gui.windows;

import com.vaadin.ui.Window;

/**
 *
 * @author xrosecky
 */
public class CustomWindow extends Window {

    public CustomWindow(String caption) {
        super(caption);
        setModal(true);
        setDraggable(false);
        setResizable(false);
        center();
        setWidth("25%");
    }
    
}
