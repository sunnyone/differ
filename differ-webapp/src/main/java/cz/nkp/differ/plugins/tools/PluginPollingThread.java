package cz.nkp.differ.plugins.tools;

import java.io.IOException;

import com.vaadin.ui.Component;
import cz.nkp.differ.compare.io.CompareComponent;
import cz.nkp.differ.gui.components.WebProgressListener;

import cz.nkp.differ.listener.ProgressListener;

public class PluginPollingThread extends Thread {

    private volatile Component comp = null;
    private volatile ProgressListener callback;
    private volatile CompareComponent plugin;

    public PluginPollingThread(final CompareComponent plugin, final ProgressListener callback) throws IOException {
        if (plugin == null || callback == null) {
            throw new IOException("Plugin or callback null!");
        }

        this.plugin = plugin;
        this.callback = callback;
    }
    volatile boolean continueRun = true;

    public void run() {
        while (continueRun) {

            if (checkCallback()) {
                continueRun = false;
            } else {
                try {
                    sleep(250);
                } catch (InterruptedException e) {
                    continueRun = false;
                }
            }

        }

    }

    private synchronized final boolean checkCallback() {
        if (comp != null) {
            return true;// Empty cycles if we have finished
        }

        plugin.waitForResults(callback);
        comp = plugin.getPluginDisplayComponent();
        if (comp != null) {
            if (callback instanceof WebProgressListener) {
                ((WebProgressListener) callback).ready(comp);
                return true;
            }
            return false;
        }

        return false;
    }
}