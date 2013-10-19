package cz.nkp.differ.dao.hibernate;

import cz.nkp.differ.dao.UserDAO;
import cz.nkp.differ.model.User;
import org.springframework.dao.support.DataAccessUtils;

/**
 *
 * @author xrosecky
 */
public class UserHibernateDAO extends GenericHibernateDAO<User, Long> implements UserDAO {

    @Override
    public User findByUserName(String username) {
        return (User) DataAccessUtils.singleResult(getHibernateTemplate()
                .find("from User where username = ?", username));
    }

}
