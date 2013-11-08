package cz.nkp.differ.compare.io;

import cz.nkp.differ.exceptions.ImageDifferException;
import cz.nkp.differ.listener.ProgressListener;
import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public class ImageProcessorWithPostProcessing extends ImageProcessor {
    
    private ImageProcessor imageProcessor;
    
    private List<ResultPostProcessor> resultPostProcessors = Collections.emptyList();

    @Override
    public ImageProcessorResult processImage(File image, ProgressListener callback) throws ImageDifferException {
        ImageProcessorResult result = imageProcessor.processImage(image, callback);
        process(result);
        return result;
    }

    @Override
    public ImageProcessorResult[] processImages(File a, File b, ProgressListener callback) throws ImageDifferException {
	ImageProcessorResult[] results = imageProcessor.processImages(a, b, callback);
	for (ImageProcessorResult result : results) {
	    if (result != null) {
		process(result);
	    }
	}
	return results;
    }
    
    private void process(ImageProcessorResult result) {
        for (ResultPostProcessor processor : resultPostProcessors) {
            processor.process(result);
        }
    }

    public ImageProcessor getImageProcessor() {
        return imageProcessor;
    }

    public void setImageProcessor(ImageProcessor imageProcessor) {
        this.imageProcessor = imageProcessor;
    }

    public List<ResultPostProcessor> getResultPostProcessors() {
        return resultPostProcessors;
    }

    public void setResultPostProcessors(List<ResultPostProcessor> resultPostProcessors) {
        this.resultPostProcessors = resultPostProcessors;
    }
    
}
