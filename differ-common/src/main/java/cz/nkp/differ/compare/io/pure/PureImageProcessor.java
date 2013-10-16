package cz.nkp.differ.compare.io.pure;

import cz.nkp.differ.compare.io.ImageProcessor;
import cz.nkp.differ.compare.io.ImageProcessorResult;
import cz.nkp.differ.compare.metadata.ImageMetadata;
import cz.nkp.differ.compare.metadata.MetadataExtractor;
import cz.nkp.differ.compare.metadata.MetadataExtractors;
import cz.nkp.differ.compare.metadata.MetadataSource;
import cz.nkp.differ.exceptions.ImageDifferException;
import cz.nkp.differ.images.ImageLoader;
import cz.nkp.differ.images.ImageManipulator;
import cz.nkp.differ.listener.EventType;
import cz.nkp.differ.listener.Message;
import cz.nkp.differ.listener.ProgressListener;
import cz.nkp.differ.listener.ProgressType;
import cz.nkp.differ.plugins.tools.ReportGenerator;
import cz.nkp.differ.tools.ExecutionTime;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implementation of ImageProcessor in Java
 *
 * @author xrosecky
 */
public class PureImageProcessor extends ImageProcessor {
    
    private static Logger logger = LogManager.getLogger(ImageProcessor.class);

    private ImageLoader imageLoader;
    private ReportGenerator pdfReporter;
    private MetadataExtractors extractors;
    private MetadataSource core = new MetadataSource(0, "", "", "core");
    
    private static class CostumProgressListener implements ProgressListener {
        
        private ProgressListener inner;
        
        private int finishedTasks = 0;

        public CostumProgressListener(ProgressListener inner) {
            this.inner = inner;
        }
        
        @Override
        public void onStart(String identifier, int numberOfTasks) {
            inner.onStart(identifier, numberOfTasks);
        }

        @Override
        public void onProgress(Message message) {
            if (message.getProgressType().equals(ProgressType.FINISH)) {
                finishedTasks++;
            }
            message.setNumberOfFinishedTaks(finishedTasks);
            inner.onProgress(message);
        }

        @Override
        public void onFinish(String identifier, ImageProcessorResult[] results) {
            inner.onFinish(identifier, results);
        }
        
    }

    public PureImageProcessor(ImageLoader imageLoader, MetadataExtractors extractors) {
        this.imageLoader = imageLoader;
        this.extractors = extractors;
        this.pdfReporter = new ReportGenerator();
    }
    
    private abstract class AbstractTask<T> implements Callable<T> {
        
        private final CostumProgressListener listener;
        private final EventType eventType;
        private final String toolName;

        public AbstractTask(CostumProgressListener listener, EventType eventType, String toolName) {
            this.listener = listener;
            this.eventType = eventType;
            this.toolName = toolName;
        }
        
        @Override
        public T call() throws Exception {
            onStart();
            try {
                return callInner();
            } finally {
                onFinish();
            }
        }
        
        protected abstract T callInner() throws Exception;
        
        protected void onStart() {
            Message message = new Message();
            message.setEventType(eventType);
            message.setProgressType(ProgressType.START);
            message.setToolName(toolName);
            listener.onProgress(message);
        }
        
        protected void onFinish() {
            Message message = new Message();
            message.setEventType(eventType);
            message.setProgressType(ProgressType.FINISH);
            message.setToolName(toolName);
            listener.onProgress(message);
        }
        
    }
    
    private class OpenImageTask extends AbstractTask<PureImageProcessorResult> {
        
        private final File image;
        private final PureImageProcessorResult result;
        private final CountDownLatch latch;
        
        public OpenImageTask(PureImageProcessorResult result, File image, CountDownLatch latch, CostumProgressListener listener) {
            super(listener, EventType.THUMBNAIL, null);
            this.result = result;
            this.image = image;
            this.latch = latch;
        }
        
        @Override
        protected PureImageProcessorResult callInner() throws Exception {
            BufferedImage fullImage = null;
            try {
                fullImage = imageLoader.load(image);
                synchronized (result) {
                    result.setFullImage(fullImage);
                }
            } catch (Exception ex) {
                PureImageProcessor.logger.error("Exception thrown when opening {}.", image.getAbsolutePath(),  ex);
                throw ex;
            } finally {
                latch.countDown();
            }
            ExecutionTime time = new ExecutionTime();
            Image preview = ImageManipulator.getBitmapScaledImage(fullImage, PureImageProcessor.this.getConfig().getImageWidth(), true);
            BufferedImage bufferedPreview = new BufferedImage(preview.getWidth(null), preview.getHeight(null), BufferedImage.TYPE_INT_RGB);
            bufferedPreview.getGraphics().drawImage(preview, 0, 0 , null);
            logger.info("Thumbnail generated in {} ms.", time.getTime());
            synchronized (result) {
                result.setPreview(bufferedPreview);
                result.setType(ImageProcessorResult.Type.IMAGE);
                result.getMetadata().add(new ImageMetadata("Image height", new Integer(fullImage.getHeight()), core));
                result.getMetadata().add(new ImageMetadata("Image width", new Integer(fullImage.getWidth()), core));
                result.getMetadata().add(new ImageMetadata("File name", image.getName(), core));
                result.getMetadata().add(new ImageMetadata("File path", image.getAbsolutePath(), core));
            }
            return result;
        }
        
    }
    
    private class ProcessImagesComparison extends AbstractTask<PureImageProcessorResult> {

        private final PureImageProcessorResult result1;
        private final PureImageProcessorResult result2;
        private final PureImageProcessorResult resultOfComparison;
        private final CountDownLatch latch;
        private final CostumProgressListener listener;

        public ProcessImagesComparison(PureImageProcessorResult[] results, CountDownLatch latch, CostumProgressListener listener) {
            super(listener, EventType.COMPARISON, null);
            this.result1 = results[0];
            this.result2 = results[1];
            this.resultOfComparison = results[2];
            this.latch = latch;
            this.listener = listener;
        }
        
        @Override
        public PureImageProcessorResult callInner() throws Exception {
            latch.await();
            try {
                getImagesDifference(result1, result2, resultOfComparison);
            } catch (IllegalArgumentException iae) {
                generateHistogramAndChecksum(result1);
                generateHistogramAndChecksum(result2);
            }
            java.awt.Image comparePreview = ImageManipulator.getBitmapScaledImage(resultOfComparison.getFullImage(), PureImageProcessor.this.getConfig().getImageWidth(), true);
            resultOfComparison.setPreview(comparePreview);
            resultOfComparison.setType(ImageProcessorResult.Type.COMPARISON);
            addMetrics(resultOfComparison);
            resultOfComparison.setType(ImageProcessorResult.Type.COMPARISON);
            return resultOfComparison;
        }

    }

    private class ImageMetadataTask extends AbstractTask<PureImageProcessorResult> {

        private MetadataExtractor extractor;
        private File image;
        private final PureImageProcessorResult result;

        public ImageMetadataTask(MetadataExtractor extractor, File image, PureImageProcessorResult result, CostumProgressListener listener) {
            super(listener, EventType.EXTERNAL_TOOL, null); //FIXME
            this.extractor = extractor;
            this.image = image;
            this.result = result;
        }

        @Override
        public PureImageProcessorResult callInner() throws Exception {
            List<ImageMetadata> metadata = extractor.getMetadata(image);
            synchronized (result) {
                result.getMetadata().addAll(metadata);
            }
            return result;
        }
    }
    
    @Override
    public PureImageProcessorResult processImage(File image, ProgressListener callback) throws ImageDifferException {
        CostumProgressListener listener = new CostumProgressListener(callback);
        listener.onStart(null, extractors.getExtractors().size());
        BufferedImage fullImage = imageLoader.load(image);
        if (fullImage == null) {
            throw new ImageDifferException(ImageDifferException.ErrorCode.IMAGE_READ_ERROR, "Unsupported file type");
        }
        java.awt.Image preview = ImageManipulator.getBitmapScaledImage(fullImage, this.getConfig().getImageWidth(), true);
        PureImageProcessorResult result = new PureImageProcessorResult(fullImage, preview);
        result.setType(ImageProcessorResult.Type.IMAGE);
        generateHistogramAndChecksum(result);

        result.getMetadata().add(new ImageMetadata("Image height", new Integer(fullImage.getHeight()), core));
        result.getMetadata().add(new ImageMetadata("Image width", new Integer(fullImage.getWidth()), core));
        result.getMetadata().add(new ImageMetadata("File name", image.getName(), core));
        result.getMetadata().add(new ImageMetadata("File path", image.getAbsolutePath(), core));
        List<Callable<PureImageProcessorResult>> tasks = new ArrayList<Callable<PureImageProcessorResult>>();
        for (MetadataExtractor extractor : extractors.getExtractors()) {
            tasks.add(new ImageMetadataTask(extractor, image, result, listener));
        }
        List<Future<PureImageProcessorResult>> futures = execute(tasks);
        markConflicts(result);
        result.setType(ImageProcessorResult.Type.IMAGE);
        pdfReporter.setDataSource(result);
        pdfReporter.buildAndExport();
        return result;
    }
    
    private void markConflicts(PureImageProcessorResult result) {
        Map<String, Object> results = new HashMap<String, Object>();
        Set<String> conflicts = new HashSet<String>();
        for (ImageMetadata data : result.getMetadata()) {
            String key = data.getKey();
            Object val1 = data.getValue();
            Object val2 = results.get(key);
            if (val2 != null && !val2.toString().equals(val1.toString())) {
                conflicts.add(key);
            }
            if (val2 == null) {
                results.put(key, val1);
            }
        }
        for (ImageMetadata data : result.getMetadata()) {
            String key = data.getKey();
            data.setConflict(conflicts.contains(key));
        }
    }

    @Override
    public ImageProcessorResult[] processImages(File file1, File file2, ProgressListener callback) throws ImageDifferException {
        CostumProgressListener listener = new CostumProgressListener(callback);
        listener.onStart(null, 3 + ( 2 * extractors.getExtractors().size()));
        ExecutionTime time = new ExecutionTime();
        logger.info("processing of images started");
        PureImageProcessorResult results[] = new PureImageProcessorResult[3];
        results[0] = new PureImageProcessorResult();
        results[1] = new PureImageProcessorResult();
        results[2] = new PureImageProcessorResult();
        CountDownLatch latch = new CountDownLatch(2);
        Callable<PureImageProcessorResult> task1 = new OpenImageTask(results[0], file1, latch, listener);
        Callable<PureImageProcessorResult> task2 = new OpenImageTask(results[1], file2, latch, listener);
        Callable<PureImageProcessorResult> lastTask = new ProcessImagesComparison(results, latch, listener);
        List<Callable<PureImageProcessorResult>> tasks = new ArrayList<Callable<PureImageProcessorResult>>();
        tasks.addAll(Arrays.asList(task1, task2, lastTask));
        for (MetadataExtractor extractor : extractors.getExtractors()) {
            tasks.add(new ImageMetadataTask(extractor, file1, results[0], listener));
            tasks.add(new ImageMetadataTask(extractor, file2, results[1], listener));
        }
        List<Future<PureImageProcessorResult>> futures = execute(tasks);
        markConflicts(results[0]);
        markConflicts(results[1]);
        logger.info("processing of images finished in {} ms.", time.getTime());
        if (results[2].getPreview() == null) {
            results[2] = null;
        }
        listener.onFinish(null, results);
        return results;
    }

    private <T> List<Future<T>> execute(List<Callable<T>> tasks) throws ImageDifferException {
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            return executor.invokeAll(tasks);
        } catch (InterruptedException ie) {
            throw new ImageDifferException(ImageDifferException.ErrorCode.IMAGE_READ_ERROR, "Timeout exceeded", ie);
        } finally {
            executor.shutdown();
        }
    }

    private void addMetrics(PureImageProcessorResult result) {
        long size = result.getHeight() * result.getWidth();
        int[][] histogram = result.getHistogram();
        String[] colours = new String[]{"red", "green", "blue"};
        MetadataSource mseSource = new MetadataSource(0, "", "", "MSE");
        MetadataSource psnrSource = new MetadataSource(0, "", "", "PSNR");
        for (int i = 0; i < 3; i++) {
            String colour = colours[i];
            long sqSum = 0;
            int[] values = histogram[i];
            for (int j = 0; j < values.length; j++) {
                sqSum += values[j] * j * j;
            }
            double mse = (1.0 / size) * sqSum;
            double psnr = 10 * Math.log10((255 * 255) / mse);
            result.getMetadata().add(new ImageMetadata(colour, mse, mseSource));
            result.getMetadata().add(new ImageMetadata(colour, psnr, psnrSource));
        }
    }

    private PureImageProcessorResult generateHistogramAndChecksum(PureImageProcessorResult result) {
        BufferedImage image = result.getFullImage();
        try {
            ExecutionTime timer = new ExecutionTime();
            MessageDigest digest = MessageDigest.getInstance("MD5");
            DigestOutputStream digestOs = new DigestOutputStream(new NullOutputStream(), digest);
            int width = image.getWidth();
            int height = image.getHeight();
            result.setWidth(width);
            result.setHeight(height);
            // histogram
            int[] imagePixelCache = new int[width * height];
            image.getRGB(0, 0, width, height, imagePixelCache, 0, width); //Get all pixels
            int[][] bins = new int[3][256];
            for (int thisPixel = 0; thisPixel < width * height; thisPixel++) {
                int rgbCombined = imagePixelCache[thisPixel];
                Color color = new Color(rgbCombined);
                bins[0][color.getRed()]++;
                bins[1][color.getGreen()]++;
                bins[2][color.getBlue()]++;
                digestOs.write(rgbCombined);
            }
            synchronized (result) {
                result.setHistogram(bins);
                result.setMD5Checksum(Hex.encodeHexString(digest.digest()));
            }
            logger.info("MD5 checksum and histogram generated in {} ms.", timer.getTime());
            return result;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void getImagesDifference(PureImageProcessorResult first, PureImageProcessorResult second, PureImageProcessorResult comparison) {
        ExecutionTime time = new ExecutionTime();
        if (first.getFullImage() == null) {
            throw new NullPointerException("first.getFullImage()");
        }
        if (second.getFullImage() == null) {
            throw new NullPointerException("second.getFullImage()");
        }
        BufferedImage image1 = first.getFullImage();
        BufferedImage image2 = second.getFullImage();
        if (image1.getWidth(null) != image2.getWidth(null)
                || image1.getHeight(null) != image2.getHeight(null)) {
            throw new IllegalArgumentException("Cannot XOR images that are differing dimensions. XOR'ing failed.");
        }
        if (image1.getTransparency() != image2.getTransparency()) {
            throw new IllegalArgumentException("Cannot XOR images that are differing transparencies. XOR'ing failed.");
        }
        try {
            int type = BufferedImage.TYPE_INT_RGB;
            if (image1.getType() != type) {
                image1 = convert(image1, type);
            }
            if (image2.getType() != type) {
                image2 = convert(image2, type);
            }
            int width = image1.getWidth(null);
            int height = image1.getHeight(null);
            int resolution = width * height;

            int[] combo1Pixels = new int[resolution];
            int[] combo2Pixels = new int[resolution];
            int[] imagePixels = new int[resolution];

            image1.getRGB(0, 0, width, height, combo1Pixels, 0, width); //Get all pixels
            image2.getRGB(0, 0, width, height, combo2Pixels, 0, width); //Get all pixels

            DigestOutputStream digest1 = new DigestOutputStream(new NullOutputStream(), MessageDigest.getInstance("MD5"));
            DigestOutputStream digest2 = new DigestOutputStream(new NullOutputStream(), MessageDigest.getInstance("MD5"));
            
            int[][] hist1 = new int[3][256];
            int[][] hist2 = new int[3][256];
            int[][] diffHist = new int[3][256];
            
            for (int pixel = 0; pixel < resolution; pixel++) {
                Color pixel1 = new Color(combo1Pixels[pixel]);
                Color pixel2 = new Color(combo2Pixels[pixel]);
                Color diff = new Color(
                        Math.abs(pixel1.getRed() - pixel2.getRed()),
                        Math.abs(pixel1.getGreen() - pixel2.getGreen()),
                        Math.abs(pixel1.getBlue() - pixel2.getBlue()));
                imagePixels[pixel] = diff.getRGB();
                
                // MD5 checksum
                digest1.write(combo1Pixels[pixel]);
                digest2.write(combo2Pixels[pixel]);
                
                // histogram
                hist1[0][pixel1.getRed()]++;
                hist1[1][pixel1.getGreen()]++;
                hist1[2][pixel1.getBlue()]++;
                
                hist2[0][pixel2.getRed()]++;
                hist2[1][pixel2.getGreen()]++;
                hist2[2][pixel2.getBlue()]++;
                
                diffHist[0][diff.getRed()]++;
                diffHist[1][diff.getGreen()]++;
                diffHist[2][diff.getBlue()]++;
            }

            int imageType = image1.getType();
            if (imageType == BufferedImage.TYPE_CUSTOM) {
                imageType = BufferedImage.TYPE_INT_RGB;
            }
            BufferedImage imageDiff = new BufferedImage(width, height, imageType);
            imageDiff.setRGB(0, 0, width, height, imagePixels, 0, width); //Set all pixels
            logger.info("Image difference processed in {} ms.", time.getTime());
            
            synchronized (first) {
                first.setMD5Checksum(Hex.encodeHexString(digest1.getMessageDigest().digest()));
                first.setHistogram(hist1);
            }
            synchronized (second) {
                second.setMD5Checksum(Hex.encodeHexString(digest2.getMessageDigest().digest()));
                second.setHistogram(hist2);
            }
            
            comparison.setFullImage(imageDiff);
            comparison.setHistogram(diffHist);
        } catch (Exception ex) {
            logger.error("Exception thrown when comparing images", ex);
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            } else {
                throw new RuntimeException(ex);
            }
        }
    }

    private static BufferedImage convert(BufferedImage image, int type) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), type);
        ColorConvertOp op = new ColorConvertOp(null);
        op.filter(image, result);
        return result;
    }

}
