package cz.nkp.differ.compare.io;

import cz.nkp.differ.compare.metadata.ImageMetadata;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public interface RefreshableResultPostProcessor<T> {

    public List<ImageMetadata> process(ImageProcessorResult result, T config);

}
