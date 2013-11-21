package cz.nkp.differ.listener;

import cz.nkp.differ.model.User;

/**
 *
 * @author xrosecky
 */
public interface LoginListener {

    public void onLogin(User user);
    
    public void onLogout(User user);
    
}
