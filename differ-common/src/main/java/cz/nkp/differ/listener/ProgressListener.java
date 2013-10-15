package cz.nkp.differ.listener;

import cz.nkp.differ.compare.io.ImageProcessorResult;

public interface ProgressListener {
    
    public void onStart(String identifier, int numberOfTasks);
    
    public void onProgress(Message message);
    
    public void onFinish(String identifier, ImageProcessorResult[] results);

}
