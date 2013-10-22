/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.nkp.differ.io;

import java.util.List;
import java.util.Map;

/**
 *
 * @author xrosecky
 */
public interface ProfileManager {

    public void deleteProfile(String name);

    public Map<String, String> getProfileByName(String name);

    public Map<String, List<String>> getProfileTemplate();

    public List<String> getProfiles();

    public void saveProfile(String name, Map<String, String> profile);
    
}
