package cz.nkp.differ.profile;

import cz.nkp.differ.compare.metadata.JP2Profile;
import cz.nkp.differ.compare.metadata.external.JP2ProfileProvider;

/**
 *
 * @author xrosecky
 */
public interface EditableJP2ProfileProvider extends JP2ProfileProvider {

    public void refresh();
    
    public void update(JP2Profile profile);
    
    public void delete(JP2Profile profile);
    
    public void saveNew(JP2Profile profile);
    
}
