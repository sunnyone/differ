package cz.nkp.differ.util;

import cz.nkp.differ.DifferApplication;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author xrosecky
 */
public class TemporaryFilesCleaner {
    
    private static class Entry {
        
        private WeakReference<File> fileRef;
        private String path;

        public Entry(File file) {
            this.fileRef = new WeakReference<File>(file);
            this.path = file.getAbsolutePath();
        }
        
        public boolean clean() {
            if (fileRef.get() == null) {
                File file = new File(path);
                file.delete();
                return true;
            } else {
                return false;
            }
        }
    }
 
    private final List<Entry> files = new ArrayList<Entry>();
    
    public void addFile(File file) {
        synchronized (files) {
            files.add(new Entry(file));
        }
        file.deleteOnExit();
    }
    
    public void clean() {
        synchronized (files) {
            ListIterator<Entry> iter = files.listIterator();
            while (iter.hasNext()) {
                if (iter.next().clean()) {
                    iter.remove();
                }
            }
        }
    }
    
    public void init() {
         TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                TemporaryFilesCleaner.this.clean();
            }
            
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 5000L, 60 * 1000L);
    }
    
}
