package cz.nkp.differ.compare.io.generators.util;

import com.vaadin.ui.Button;

/**
 *
 * @author xrosecky
 */
public class SortableButton extends Button implements Comparable<SortableButton> {
    
    private final String label;
        
    private final Object data;

    public SortableButton(String label, Object data) {
        super(label);
        this.label = label;
        this.data = data;
    }

    @Override
    public int compareTo(SortableButton other) {
        return label.compareTo(other.label);
    }
    
    @Override
    public Object getData() {
        return data;
    }

}
