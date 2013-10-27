/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.nkp.differ.user;

import cz.nkp.differ.exceptions.UserDifferException;
import cz.nkp.differ.model.User;

/**
 *
 * @author xrosecky
 */
public interface UserManager {

    /**
     * Checks the supplied username and password against the user database.
     *
     * @param username
     * @param userSuppliedPassword
     * @return
     */
    public User attemptLogin(String username, String userSuppliedPassword) throws UserDifferException;

    public User registerUser(User user, String passwordPlaintext) throws UserDifferException;
    
    public User findByUserName(String name); 
    
}
