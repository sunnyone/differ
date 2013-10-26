package cz.nkp.differ.gui.components;

import com.vaadin.data.Validator;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Form;
import com.vaadin.ui.Layout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import cz.nkp.differ.compare.metadata.JP2Profile;
import cz.nkp.differ.profile.JP2FixedTemplate;
import cz.nkp.differ.profile.JP2Template;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public class JP2ProfileForm {
    
    private static class CustomComboBox<T> extends ComboBox {
        
        public CustomComboBox(String name, List<T> values, T selectedValue) {
            super(name);
            for (T value : values) {
                super.addItem(value);
            }
            super.select(selectedValue);
        }
        
    }
    
    private static class CustomListSelect<T> extends ListSelect {

        public CustomListSelect(String name, List<T> values, List<T> selectedValues, boolean multiSelect) {
            super(name);
            super.setMultiSelect(multiSelect);
            for (T value : values) {
                super.addItem(value);
            }
            for (T selectedValue : selectedValues) {
                super.select(selectedValue);
            }
            super.setRows(values.size());
        }
    }
    
    private static class IntegerValidator implements Validator {

        @Override
        public void validate(Object value) throws InvalidValueException {
            if (!isValid(value)) {
                    throw new InvalidValueException(String.format("%s is not number", value));
            }
        }

        @Override
        public boolean isValid(Object value) {
            if (value instanceof String) {
                try {
                    Integer val = Integer.parseInt((String) value);
                    return true;
                } catch (NumberFormatException nfe) {
                    return false;
                }
            } else {
                return false;
            }
        }
        
    }
    
    private JP2Profile jp2Profile;
    
    public JP2ProfileForm(JP2Profile profile) {
        this.jp2Profile = profile;
    }
    
    public Layout createProfileCreationWindowForm() {
        Form form = new Form();
        
        JP2Template jp2Template = new JP2FixedTemplate();
        
        //Kernel
        ComboBox kernelComboBox = new CustomComboBox("Kernel", jp2Template.getKernels(), jp2Profile.getKernel());
        kernelComboBox.select(jp2Profile.getKernel());
        form.addField("kernel", kernelComboBox);
        
        // Preccints
        ListSelect preccintsListSelect = new CustomListSelect("Preccint sizes", jp2Template.getPreccints(), jp2Profile.getPreccintSizes(),  true);
        form.addField("preccintSizes", preccintsListSelect);
        
        // Tiles
        CustomListSelect tilesListSelect = new CustomListSelect("Tile sizes", jp2Template.getTiles(), jp2Profile.getTileSizes(), true);
        form.addField("tileSizes", tilesListSelect);
        
        // Min quality layers
        TextField minQualityLayersTextField = new TextField("Minimal number of quality layers");
        minQualityLayersTextField.setValue(Integer.toString(jp2Profile.getMinQualityLayers()));
        minQualityLayersTextField.addValidator(new IntegerValidator());
        form.addField("minQualityLayers", minQualityLayersTextField);
        
        // Max quality layers
        TextField maxQualityLayersTextField = new TextField("Maximal number of quality layers");
        maxQualityLayersTextField.setValue(Integer.toString(jp2Profile.getMaxQualityLayers()));
        maxQualityLayersTextField.addValidator(new IntegerValidator());
        form.addField("maxQualityLayers", maxQualityLayersTextField);
        
        form.setValidationVisible(true);
        
        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(form);
        return layout;
    }
    
}
