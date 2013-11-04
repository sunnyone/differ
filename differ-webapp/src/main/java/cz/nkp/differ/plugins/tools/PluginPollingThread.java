package cz.nkp.differ.plugins.tools;

import com.vaadin.ui.Component;
import cz.nkp.differ.compare.io.CompareComponent;
import cz.nkp.differ.gui.components.WebProgressListener;
import cz.nkp.differ.listener.ProgressListener;

public class PluginPollingThread implements Runnable {

    private ProgressListener callback;
    private CompareComponent plugin;

    public PluginPollingThread(final CompareComponent plugin, final ProgressListener callback) {
        if (plugin == null) {
            throw new NullPointerException("plugin");
        }
        if (callback == null) {
            throw new NullPointerException("callback");
        }
        this.plugin = plugin;
        this.callback = callback;
    }

    @Override
    public void run() {
        plugin.waitForResults(callback);
        Component comp = plugin.getPluginDisplayComponent();
        if (comp != null) {
            if (callback instanceof WebProgressListener) {
                ((WebProgressListener) callback).ready(comp);
            }
        }
    }

}