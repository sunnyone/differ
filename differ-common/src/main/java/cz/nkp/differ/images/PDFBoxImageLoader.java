/**
 * author Jan Stavěl <stavel.jan@gmail.com>
 */
package cz.nkp.differ.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFImageWriter;

import cz.nkp.differ.exceptions.ImageDifferException;

/**
 * @author Jan Stavěl <stavel.jan@gmail.com>
 *
 */
public class PDFBoxImageLoader implements ImageLoader {

	/* (non-Javadoc)
	 * @see cz.nkp.differ.images.ImageLoader#load(java.io.File)
	 */
	@Override
	public BufferedImage load(File file) throws ImageDifferException {
            try {
                PDDocument document = PDDocument.load(file);
                int imageType = BufferedImage.TYPE_INT_RGB;
                PDFImageWriter imageWriter = new PDFImageWriter();
                String filePath = file.getAbsolutePath();
                String prefix = filePath.substring(0, filePath.lastIndexOf('.'));
                boolean success = imageWriter.writeImage(document, "jpg", "",1,1,prefix,imageType,96);
                if (!success){
                    throw new IOException("Error: no writer found for image format 'jpg'" );
                }
                BufferedImage image = ImageIO.read(new File(prefix + "1.jpg"));
                return image;
            } catch (IOException e) {
                throw new ImageDifferException(ImageDifferException.ErrorCode.IMAGE_READ_ERROR,
                                               String.format("Error reading image: %s", file.getAbsolutePath()), e);
            }
	}
}
