package cz.nkp.differ.compare.metadata;

import java.io.File;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public interface MetadataExtractor {

    public String getSource();
    
    public List<ImageMetadata> getMetadata(File imageFile);

}
