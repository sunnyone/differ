package cz.nkp.differ.compare.metadata.external;

import cz.nkp.differ.compare.metadata.JP2Profile;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public interface JP2ProfileProvider {

    public List<JP2Profile> getProfiles();
    
}
