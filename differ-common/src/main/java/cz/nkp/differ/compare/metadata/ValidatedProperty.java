package cz.nkp.differ.compare.metadata;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author xrosecky
 */
@XmlRootElement(name = "validated-property")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class ValidatedProperty {

    @XmlElement(name = "value")
    public String value;
    
    @XmlElement(name = "valid")
    public boolean valid;

    public ValidatedProperty() {
        
    }
    
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
