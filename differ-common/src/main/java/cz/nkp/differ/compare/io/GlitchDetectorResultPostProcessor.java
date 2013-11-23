package cz.nkp.differ.compare.io;

import cz.nkp.differ.compare.metadata.ImageMetadata;
import cz.nkp.differ.compare.metadata.MetadataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public class GlitchDetectorResultPostProcessor implements ResultPostProcessor, RefreshableResultPostProcessor<GlitchDetectorConfig> {
    
    public static final String SOURCE_NAME = "Glitch detector";

    private static final MetadataSource source = new MetadataSource(SOURCE_NAME);
    
    private static final String[] colours = new String[]{ "Max red ratio", "Max green ratio", "Max blue ratio" };

    private GlitchDetectorConfig config = new GlitchDetectorConfig();

    @Override
    public List<ImageMetadata> process(ImageProcessorResult result, GlitchDetectorConfig glitchDetectorConfig) {
	List<ImageMetadata> metadata = new ArrayList<ImageMetadata>();
	if (result.getType().equals(ImageProcessorResult.Type.COMPARISON)) {
            return Collections.emptyList();
        }
        boolean glitchDetected = false;
        int resolution = result.getHeight() * result.getWidth();
        int[] grayscaleHistogram = result.getBlackAndWhiteHistogram();
        if (grayscaleHistogram != null) {
            if (grayscaleHistogram[0] == resolution || grayscaleHistogram[255] == resolution ) {
                glitchDetected = true;
            }
	    ImageMetadata white = createImageMetadataEntry("Number of absolute white pixels", Integer.toString(grayscaleHistogram[0]));
	    metadata.add(white);
	    ImageMetadata black = createImageMetadataEntry("Number of absolute black pixels", Integer.toString(grayscaleHistogram[255]));
	    metadata.add(black);
        }
        int[][] colorHistogram = result.getHistogram();
        if (colorHistogram != null) {
            for (int i = 0; i != 3; i++) {
                double ratio = colorHistogram[i][255] / (resolution * 1.0);
		ImageMetadata entry = createImageMetadataEntry(colours[i], Double.toString(ratio * 100));
		metadata.add(entry);
		double maxAllowedRatio = 1.0;
		switch (i) {
		    case 0: maxAllowedRatio = glitchDetectorConfig.getMaxAllowedRatioForAbsoluteRed(); break;
		    case 1: maxAllowedRatio = glitchDetectorConfig.getMaxAllowedRatioForAbsoluteGreen(); break;
		    case 2: maxAllowedRatio = glitchDetectorConfig.getMaxAllowedRatioForAbsoluteBlue(); break;
		}
                if (ratio > maxAllowedRatio) {
                    glitchDetected = true;
                }
            }
        }
        ImageMetadata entry = new ImageMetadata();
        entry.setSource(source);
        entry.setKey("Glitch detected");
        entry.setValue(glitchDetected);
	metadata.add(entry);
	return metadata;
    }

    @Override
    public void process(ImageProcessorResult result) {
        result.getMetadata().addAll(process(result, config));
    }

    public GlitchDetectorConfig getConfig() {
	return config;
    }

    public void setConfig(GlitchDetectorConfig config) {
	this.config = config;
    }
    
    private ImageMetadata createImageMetadataEntry(String key, String value) {
	ImageMetadata entry = new ImageMetadata();
        entry.setSource(source);
        entry.setKey(key);
        entry.setValue(value);
	return entry;
    }
    
}
