package cz.nkp.differ.compare.io.external;

import cz.nkp.differ.compare.io.ImagesComparator;
import cz.nkp.differ.compare.metadata.ImageMetadata;
import cz.nkp.differ.compare.metadata.external.ExternalMetadataExtractor;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author xrosecky
 */
public class ExternalImagesComparator implements ImagesComparator {

    public ExternalMetadataExtractor extractor;

    @Override
    public List<ImageMetadata> getMetadata(File file1, File file2) {
	Map<String, String> attributes = new HashMap<String, String>();
	attributes.put("{file1}", file1.getAbsolutePath());
	attributes.put("{file2}", file2.getAbsolutePath());
	List<ImageMetadata> metadata = extractor.getMetadata(attributes);
	return metadata;
    }

    @Override
    public String getSource() {
	return extractor.getSource();
    }

    public ExternalMetadataExtractor getExtractor() {
	return extractor;
    }

    public void setExtractor(ExternalMetadataExtractor extractor) {
	this.extractor = extractor;
    }

}
