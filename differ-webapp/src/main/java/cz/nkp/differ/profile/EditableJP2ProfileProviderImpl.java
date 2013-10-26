package cz.nkp.differ.profile;

import cz.nkp.differ.compare.metadata.JP2Kernel;
import cz.nkp.differ.compare.metadata.JP2Profile;
import cz.nkp.differ.compare.metadata.JP2Size;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public class EditableJP2ProfileProviderImpl implements EditableJP2ProfileProvider {

    private List<JP2Profile> profiles = new ArrayList<JP2Profile>();

    public EditableJP2ProfileProviderImpl() {
        JP2Profile profile1 = new JP2Profile();
        profile1.setKernel(JP2Kernel.Irreversible9x7);
        profile1.setMinQualityLayers(1);
        profile1.setMaxQualityLayers(1);
        profile1.setTileSizes(Arrays.asList(new JP2Size(1024, 1024), new JP2Size(4096, 4096)));
        profile1.setPreccintSizes(Arrays.asList(new JP2Size(64, 64)));
        profile1.setName("1");
        profiles.add(profile1);

        JP2Profile profile2 = new JP2Profile();
        profile2.setKernel(JP2Kernel.Revesible5x3);
        profile2.setMinQualityLayers(1);
        profile2.setMaxQualityLayers(4);
        profile2.setTileSizes(Arrays.asList(new JP2Size(256, 256), new JP2Size(1024, 1024)));
        profile2.setPreccintSizes(Arrays.asList(new JP2Size(64, 64)));
        profile2.setName("2");
        profiles.add(profile2);
    }
    
    @Override
    public void refresh() {
    }

    @Override
    public void update(JP2Profile profile) {
    }

    @Override
    public void saveNew(JP2Profile profile) {
        profiles.add(profile);
    }

    @Override
    public List<JP2Profile> getProfiles() {
        return profiles;
    }
    
}
