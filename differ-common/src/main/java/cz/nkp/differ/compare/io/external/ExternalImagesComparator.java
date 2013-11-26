package cz.nkp.differ.compare.io.external;

import cz.nkp.differ.compare.io.ImagesComparator;
import cz.nkp.differ.compare.metadata.ImageMetadata;
import cz.nkp.differ.compare.metadata.MetadataExtractorWithAttributes;
import cz.nkp.differ.exceptions.ImageDifferException;
import cz.nkp.differ.images.ImageLoader;
import cz.nkp.differ.images.ImageLoaderFactory;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author xrosecky
 */
public class ExternalImagesComparator implements ImagesComparator {
    
    private static Logger logger = LogManager.getLogger(ImagesComparator.class);

    private static final String PREFIX = "differ";
    
    private MetadataExtractorWithAttributes extractor;
    
    private ImageLoader imageLoader;
    
    private String suffix = "bmp";
    
    private List<String> supportedExtensions = null;

    @Override
    public List<ImageMetadata> getMetadata(File file1, File file2) {
        File tmpFile1 = null;
        File tmpFile2 = null;
        try {
            tmpFile1 = convertFileIfNotSupported(file1);
            tmpFile2 = convertFileIfNotSupported(file2);
            if (tmpFile1 != null) {
                file1 = tmpFile1;
            }
            if (tmpFile2 != null) {
                file2 = tmpFile2;
            }
            Map<String, String> attributes = new HashMap<String, String>();
            attributes.put("{file1}", file1.getAbsolutePath());
            attributes.put("{file2}", file2.getAbsolutePath());
            List<ImageMetadata> metadata = extractor.getMetadata(attributes);
            return metadata;
        } finally {
            if (tmpFile1 != null) {
                tmpFile1.delete();
            }
            if (tmpFile2 != null) {
                tmpFile2.delete();
            }
        }
    }
    
    private File convertFileIfNotSupported(File file) {
        File result = null;
        String extension = ImageLoaderFactory.getExtension(file.getName()).toLowerCase();
        if (supportedExtensions != null && !supportedExtensions.contains(extension)) {
            try {
                logger.info("About to convert unsupported image type for file '{}'", file);
                BufferedImage img = imageLoader.load(file);
                File tmpFile = File.createTempFile(PREFIX, '.' + suffix);
                ImageIO.write(img, suffix, result);
                result = tmpFile;
                logger.info("Unsupported image type converted to '{}'.", tmpFile);
            } catch (Exception ex) {
                logger.error("Exception thrown when converting image.", ex);
            }
        }
        return result;
    }

    @Override
    public String getSource() {
	return extractor.getSource();
    }

    public MetadataExtractorWithAttributes getExtractor() {
	return extractor;
    }

    public void setExtractor(MetadataExtractorWithAttributes extractor) {
	this.extractor = extractor;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public List<String> getSupportedExtensions() {
        return supportedExtensions;
    }

    public void setSupportedExtensions(List<String> supportedExtensions) {
        this.supportedExtensions = supportedExtensions;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    
}
