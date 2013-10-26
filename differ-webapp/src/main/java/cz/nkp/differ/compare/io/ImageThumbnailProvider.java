package cz.nkp.differ.compare.io;

import com.vaadin.terminal.Resource;
import cz.nkp.differ.exceptions.ImageDifferException;
import cz.nkp.differ.images.ImageLoaderFactory;
import cz.nkp.differ.images.ImageManipulator;
import cz.nkp.differ.util.GUIMacros;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 *
 * @author xrosecky
 * 
 * FIXME: move to interface + cache thumbnails
 */
public class ImageThumbnailProvider {
    
    private ImageLoaderFactory imageLoaderFactory = null;
    
    private int width = 100;
    
    public Resource getThumbnail(File file) throws ImageDifferException {
        BufferedImage image = imageLoaderFactory.load(file);
        if (image != null) {
            java.awt.Image thumbnail = ImageManipulator.getBitmapScaledImage(image, width, true);
            Resource result = GUIMacros.imageToResource(thumbnail);
            return result;
        }
        return null;
    }

    public ImageLoaderFactory getImageLoaderFactory() {
        return imageLoaderFactory;
    }

    public void setImageLoaderFactory(ImageLoaderFactory imageLoaderFactory) {
        this.imageLoaderFactory = imageLoaderFactory;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    
}
