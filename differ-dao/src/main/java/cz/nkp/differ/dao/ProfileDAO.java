package cz.nkp.differ.dao;

import cz.nkp.differ.model.Profile;
import cz.nkp.differ.model.User;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public interface ProfileDAO extends GenericDAO<Profile, Long> {

    public List<Profile> findByUser(User user);

    public List<Profile> findAllShared();
    
}
