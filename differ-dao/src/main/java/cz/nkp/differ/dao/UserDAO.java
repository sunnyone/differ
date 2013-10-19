package cz.nkp.differ.dao;

import cz.nkp.differ.model.User;

/**
 *
 * @author xrosecky
 */
public interface UserDAO extends GenericDAO<User, Long> {

    public User findByUserName(String userName);

}
