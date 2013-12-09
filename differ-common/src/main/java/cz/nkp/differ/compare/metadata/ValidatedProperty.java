package cz.nkp.differ.compare.metadata;

/**
 *
 * @author xrosecky
 */
public class ValidatedProperty {

    public String value;
    
    public boolean valid;

    public ValidatedProperty(String value, boolean valid) {
        this.value = value;
        this.valid = valid;
    }
    
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public String toString() {
        return value;
    }
    
}
