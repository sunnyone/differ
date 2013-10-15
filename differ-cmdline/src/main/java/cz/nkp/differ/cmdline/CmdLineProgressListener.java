package cz.nkp.differ.cmdline;

import cz.nkp.differ.compare.io.ImageProcessorResult;
import cz.nkp.differ.listener.Message;
import cz.nkp.differ.listener.ProgressListener;
import cz.nkp.differ.listener.ProgressType;

/**
 *
 * @author xrosecky
 */
public class CmdLineProgressListener implements ProgressListener {

    private int numberOfTasks = 0;
    
    @Override
    public void onStart(String identifier, int numberOfTasks) {
        this.numberOfTasks = numberOfTasks;
    }

    @Override
    public void onProgress(Message message) {
        if (message.getProgressType().equals(ProgressType.FINISH)) {
            System.err.println(String.format("[%s/%s] %s", message.getNumberOfFinishedTaks(),
                    numberOfTasks, message.getEventType()));
        }
    }

    @Override
    public void onFinish(String identifier, ImageProcessorResult[] results) {
    }
    
}
