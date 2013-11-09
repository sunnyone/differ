package cz.nkp.differ.compare.metadata;

import java.util.List;
import java.util.Map;

/**
 *
 * @author xrosecky
 */
public interface MetadataExtractorWithAttributes extends MetadataExtractor {

    public List<ImageMetadata> getMetadata(Map<String, String> attributes);

}
