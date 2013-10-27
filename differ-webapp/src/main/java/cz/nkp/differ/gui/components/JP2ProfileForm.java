package cz.nkp.differ.gui.components;

import com.vaadin.data.Validator;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Form;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;
import cz.nkp.differ.compare.metadata.JP2Kernel;
import cz.nkp.differ.compare.metadata.JP2Profile;
import cz.nkp.differ.compare.metadata.JP2Size;
import cz.nkp.differ.profile.JP2FixedTemplate;
import cz.nkp.differ.profile.JP2Template;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author xrosecky
 */
public class JP2ProfileForm {
    
    private JP2Profile jp2Profile;
    
    private Form profileForm;
    
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
            if (selectedValues != null) {
                for (T selectedValue : selectedValues) {
                    super.select(selectedValue);
                }
            }
            super.setRows(values.size());
            super.setWidth("100px");
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
    
    public JP2ProfileForm(JP2Profile profile) {
        this.jp2Profile = profile;
        this.profileForm = createProfileCreationWindowForm();
    }
    
    private Form createProfileCreationWindowForm() {
        JP2Template jp2Template = new JP2FixedTemplate();
      
        Form form = new Form();
        // Kernel
        ComboBox kernel = new CustomComboBox("Kernel", jp2Template.getKernels(), jp2Profile.getKernel());
        kernel.select(jp2Profile.getKernel());
        form.addField("kernel", kernel);
        
        // Preccints
        ListSelect preccintSizes = new CustomListSelect("Preccint sizes", jp2Template.getPreccintSizes(), jp2Profile.getPreccintSizes(),  true);
        form.addField("preccintSizes", preccintSizes);
        
        // Tiles
        CustomListSelect tileSizes = new CustomListSelect("Tile sizes", jp2Template.getTileSizes(), jp2Profile.getTileSizes(), true);
        form.addField("tileSizes", tileSizes);
        
        // Decomposition levels
        CustomListSelect decompositionLevels = new CustomListSelect("Decomposition levels", jp2Template.getDecompositionLevels(), jp2Profile.getDecompositionLevels(), true);
        form.addField("decompositionLevels", decompositionLevels);
        
        // Progression orders
        CustomListSelect progressionOrders = new CustomListSelect("Progression orders", jp2Template.getProgressionOrders(), jp2Profile.getProgressionOrders(), true);
        form.addField("progressionOrders", progressionOrders);
        
        // Min quality layers
        TextField minQualityLayers = new TextField("Minimal number of quality layers");
        minQualityLayers.setValue(Integer.toString(jp2Profile.getMinQualityLayers()));
        minQualityLayers.addValidator(new IntegerValidator());
        form.addField("minQualityLayers", minQualityLayers);
        
        // Max quality layers
        TextField maxQualityLayers = new TextField("Maximal number of quality layers");
        maxQualityLayers.setValue(Integer.toString(jp2Profile.getMaxQualityLayers()));
        maxQualityLayers.addValidator(new IntegerValidator());
        form.addField("maxQualityLayers", maxQualityLayers);
        
        form.setValidationVisible(true);
        return form;
    }
    
    public Form getProfileForm() {
        return profileForm;
    }
    
    public JP2Profile getJp2Profile() {
        if (profileForm.isValid()) {
            JP2Kernel kernel = (JP2Kernel) profileForm.getField("kernel").getValue();
            jp2Profile.setKernel(kernel);
            
            Set<JP2Size> preccintSizes = (Set<JP2Size>) profileForm.getField("preccintSizes").getValue();
            jp2Profile.setPreccintSizes(toList(preccintSizes));
            
            Set<JP2Size> tileSizes = (Set<JP2Size>) profileForm.getField("tileSizes").getValue();
            jp2Profile.setTileSizes(toList(tileSizes));
            
            Set<String> progressionOrders = (Set<String>) profileForm.getField("progressionOrders").getValue();
            jp2Profile.setProgressionOrders(toList(progressionOrders));
            
            Set<Integer> decompositionLevels = (Set<Integer>) profileForm.getField("decompositionLevels").getValue();
            jp2Profile.setDecompositionLevels(toList(decompositionLevels));
            
            int minQualityLayers = Integer.parseInt((String) profileForm.getField("minQualityLayers").getValue());
            int maxQualityLayers = Integer.parseInt((String) profileForm.getField("maxQualityLayers").getValue());
            jp2Profile.setMinQualityLayers(minQualityLayers);
            jp2Profile.setMaxQualityLayers(maxQualityLayers);
            
            return jp2Profile;
        } else {
            return null;
        }
    }
    
    private <T> List<T> toList(Set<T> set) {
        List<T> result = new ArrayList<T>();
        result.addAll(set);
        return result;
    }
    
}
