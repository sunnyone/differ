package cz.nkp.differ.dao;

import cz.nkp.differ.dao.hibernate.GenericHibernateDAO;
import cz.nkp.differ.exceptions.UserDifferException;
import cz.nkp.differ.model.User;

/**
 *
 * @author xrosecky
 */
public class UserDAOImpl extends GenericHibernateDAO<User, Long> implements UserDAO {

    @Override
    public User findUserByUserName(String userName) throws UserDifferException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
