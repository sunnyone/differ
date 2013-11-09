package cz.nkp.differ.compare.io;

import cz.nkp.differ.compare.metadata.ImageMetadata;
import java.io.File;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public interface ImagesComparator {

    public String getSource();

    public List<ImageMetadata> getMetadata(File file1, File file2);

}
