package cz.nkp.differ.compare.metadata;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.InitializingBean;

/**
 *
 * @author xrosecky
 */
public class MetadataGroups implements InitializingBean {
    
    private Set<String> extractorProperties;
    
    private Set<String> identificationProperties;
    
    private Set<String> validationProperties;
    
    private Set<String> characterizationProperties;
    
    private Set<String> allProperties;

    public Set<String> getExtractorProperties() {
        return extractorProperties;
    }

    public void setExtractorProperties(Set<String> extractorProperties) {
        this.extractorProperties = extractorProperties;
    }

    public Set<String> getIdentificationProperties() {
        return identificationProperties;
    }

    public void setIdentificationProperties(Set<String> identificationProperties) {
        this.identificationProperties = identificationProperties;
    }

    public Set<String> getValidationProperties() {
        return validationProperties;
    }

    public void setValidationProperties(Set<String> validationProperties) {
        this.validationProperties = validationProperties;
    }

    public Set<String> getCharacterizationProperties() {
        return characterizationProperties;
    }

    public void setCharacterizationProperties(Set<String> characterizationProperties) {
        this.characterizationProperties = characterizationProperties;
    }
    
    public Set<String> getAllProperties() {
        return this.allProperties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        allProperties = new HashSet<String>();
        allProperties.addAll(this.characterizationProperties);
        allProperties.addAll(this.extractorProperties);
        allProperties.addAll(this.identificationProperties);
        allProperties.addAll(this.validationProperties);
    }
}
