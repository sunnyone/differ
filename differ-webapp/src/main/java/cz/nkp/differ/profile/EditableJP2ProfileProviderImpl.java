package cz.nkp.differ.profile;

import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.compare.metadata.JP2Profile;
import cz.nkp.differ.dao.ProfileDAO;
import cz.nkp.differ.model.Profile;
import cz.nkp.differ.model.User;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author xrosecky
 */
@Transactional
public class EditableJP2ProfileProviderImpl implements EditableJP2ProfileProvider {
    
    @Autowired
    private ProfileDAO profileDao;
    
    @Autowired
    private Jaxb2Marshaller marshaller;

    public EditableJP2ProfileProviderImpl() {
    }
    
    @Override
    public void refresh() {
    }

    @Override
    public void update(JP2Profile profile) {
        Profile profileInDb = profileDao.findById(profile.getId());
        marshal(profile, profileInDb);
        profileDao.persist(profileInDb);
        profile.setId(profileInDb.getId());
    }

    @Override
    public void delete(JP2Profile profile) {
        Profile profileInDb = profileDao.findById(profile.getId());
        profileDao.delete(profileInDb);
    }
    
    @Override
    public void saveNew(JP2Profile profile) {
        Profile profileInDb = new Profile();
        profileInDb.setShared(true);
        User user = DifferApplication.getCurrentApplication().getLoggedUser();
        if (user != null) {
            profileInDb.setUserId(user.getId());
        }
        marshal(profile, profileInDb);
        profileDao.persist(profileInDb);
        profile.setId(profileInDb.getId());
    }

    @Override
    public List<JP2Profile> getProfiles() {
        List<Profile> profilesInDb = profileDao.findAllShared();
        List<JP2Profile> result = new ArrayList<JP2Profile>();
        for (Profile profile : profilesInDb) {
            result.add(unmarshal(profile));
        }
        return result;
    }
    
    private JP2Profile unmarshal(Profile profile) {
        JP2Profile jp2Profile = (JP2Profile) marshaller.unmarshal(new StreamSource(new StringReader(profile.getProfile())));
        jp2Profile.setId(profile.getId());
        return jp2Profile;
    }
    
    private void marshal(JP2Profile jp2Profile, Profile profileInDb) {
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        marshaller.marshal(jp2Profile, result);
        profileInDb.setProfile(writer.toString());
    }
    
}
