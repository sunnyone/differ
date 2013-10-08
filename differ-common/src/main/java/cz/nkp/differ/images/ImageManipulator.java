package cz.nkp.differ.images;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ImageManipulator {

    private static Logger LOGGER = LogManager.getLogger(ImageManipulator.class);

    public static class ImageManipulationException extends Exception {

	public ImageManipulationException(String string) {
	    super(string);
	    LOGGER.warn(string);
	}
    }

    public static Image getBitmapScaledImage(BufferedImage image, int width, boolean scaleFit) {
        if (image == null) {
            throw new NullPointerException("image");
        }
	if (width < 1) {
	    throw new IllegalArgumentException("Invalid image scale width. Image scaling failed");
	}
	int height = image.getHeight();
	if (scaleFit) {
	    double scale = ((double) width / ((double) image.getWidth()));
	    height = (int) (image.getHeight() * scale);
	}
	return image.getScaledInstance(width, height, BufferedImage.SCALE_FAST);
    }
}
