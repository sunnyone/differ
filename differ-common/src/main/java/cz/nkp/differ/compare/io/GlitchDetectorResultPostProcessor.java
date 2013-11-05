package cz.nkp.differ.compare.io;

import cz.nkp.differ.compare.metadata.ImageMetadata;
import cz.nkp.differ.compare.metadata.MetadataSource;

/**
 *
 * @author xrosecky
 */
public class GlitchDetectorResultPostProcessor implements ResultPostProcessor {

    private static final MetadataSource source = new MetadataSource("glitch-detector");
    
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
        }
        int[][] colorHistogram = result.getHistogram();
        if (colorHistogram != null) {
            for (int i = 0; i != 3; i++) {
                double ratio = colorHistogram[i][255] / (resolution * 1.0);
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
    
}
