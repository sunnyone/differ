package cz.nkp.differ.compare.io;

import cz.nkp.differ.compare.metadata.ImageMetadata;
import java.awt.Image;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public interface ImageProcessorResult {

    enum Type {

	IMAGE,
	COMPARISON
    };

    public Type getType();

    public int[][] getHistogram();
    
    public int[] getBlackAndWhiteHistogram();

    public Image getPreview();

    public Image getFullImage();

    public String getMD5Checksum();

    public int getWidth();

    public int getHeight();

    public List<ImageMetadata> getMetadata();
}
