package cz.nkp.differ.listener;

import cz.nkp.differ.compare.io.ImageProcessorResult;

/**
 *
 * @author xrosecky
 */
public class NullProgressListener implements ProgressListener {

    @Override
    public void onStart(String identifier, int numberOfTasks) {
    }

    @Override
    public void onProgress(Message message) {
    }

    @Override
    public void onFinish(String identifier, ImageProcessorResult[] results) {
    }
    
}
