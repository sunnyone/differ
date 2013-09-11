package cz.nkp.differ.compare.metadata.external;

import cz.nkp.differ.compare.metadata.JP2Profile;
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.stream.StreamSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

/**
 *
 * @author xrosecky
 */
public class StaticJP2ProfileProvider implements JP2ProfileProvider, InitializingBean {

    List<String> resources;
    
    List<JP2Profile> profiles;
    
    @Autowired
    private Jaxb2Marshaller marshaller;
    
    @Autowired
    private ResourceLoader resourceLoader;
    
    @Override
    public List<JP2Profile> getProfiles() {
        return profiles;
    }

    public List<String> getResources() {
        return resources;
    }

    public void setResources(List<String> resources) {
        this.resources = resources;
    } 

    @Override
    public void afterPropertiesSet() throws Exception {
        profiles = new ArrayList<JP2Profile>();
        for (String res : resources) {
            Resource resource = resourceLoader.getResource(res);
            JP2Profile profile = (JP2Profile) marshaller.unmarshal(new StreamSource(resource.getInputStream()));
            profiles.add(profile);
        }
    }
    
}
