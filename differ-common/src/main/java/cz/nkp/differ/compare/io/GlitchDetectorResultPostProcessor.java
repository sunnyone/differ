package cz.nkp.differ.compare.io;

import cz.nkp.differ.compare.metadata.ImageMetadata;
import cz.nkp.differ.compare.metadata.MetadataSource;

/**
 *
 * @author xrosecky
 */
public class GlitchDetectorResultPostProcessor implements ResultPostProcessor {
    
    public static final String SOURCE_NAME = "Glitch detector";

    private static final MetadataSource source = new MetadataSource(SOURCE_NAME);
    
    private static final String[] colours = new String[]{ "Max red ratio", "Max green ratio", "Max blue ratio" };
    
    private double maxAllowedRatio = 0.6;
    
    @Override
    public void process(ImageProcessorResult result) {
        if (result.getType().equals(ImageProcessorResult.Type.COMPARISON)) {
            return;
        }
        boolean glitchDetected = false;
        int resolution = result.getHeight() * result.getWidth();
        int[] grayscaleHistogram = result.getBlackAndWhiteHistogram();
        if (grayscaleHistogram != null) {
            if (grayscaleHistogram[0] == resolution || grayscaleHistogram[255] == resolution ) {
                glitchDetected = true;
            }
	    ImageMetadata white = createImageMetadataEntry("Number of absolute white pixels", Integer.toString(grayscaleHistogram[0]));
	    result.getMetadata().add(white);
	    ImageMetadata black = createImageMetadataEntry("Number of absolute black pixels", Integer.toString(grayscaleHistogram[255]));
	    result.getMetadata().add(black);
        }
        int[][] colorHistogram = result.getHistogram();
        if (colorHistogram != null) {
            for (int i = 0; i != 3; i++) {
                double ratio = colorHistogram[i][255] / (resolution * 1.0);
		ImageMetadata entry = createImageMetadataEntry(colours[i], Double.toString(ratio * 100));
		result.getMetadata().add(entry);
                if (ratio > maxAllowedRatio) {
                    glitchDetected = true;
                }
            }
        }
        ImageMetadata entry = new ImageMetadata();
        entry.setSource(source);
        entry.setKey("Glitch detected");
        entry.setValue(glitchDetected);
        result.getMetadata().add(entry);
    }
    
    private ImageMetadata createImageMetadataEntry(String key, String value) {
	ImageMetadata entry = new ImageMetadata();
        entry.setSource(source);
        entry.setKey(key);
        entry.setValue(value);
	return entry;
    }
    
}
