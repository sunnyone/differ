package cz.nkp.differ.dao.hibernate;

import cz.nkp.differ.dao.ProfileDAO;
import cz.nkp.differ.model.Profile;
import cz.nkp.differ.model.User;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public class ProfileHibernateDAO extends GenericHibernateDAO<Profile, Long> implements ProfileDAO  {

    @Override
    public List<Profile> findByUser(User user) {
        return getHibernateTemplate().find("from Profile where userId = ?", user.getId());
    }

    @Override
    public List<Profile> findAllShared() {
        return (List<Profile>) getHibernateTemplate().find("from Profile where shared = true");
    }
    
}
