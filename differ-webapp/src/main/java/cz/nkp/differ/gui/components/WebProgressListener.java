package cz.nkp.differ.gui.components;

import cz.nkp.differ.listener.ProgressListener;

public interface WebProgressListener extends ProgressListener {

    public void onReady(Object c);
    
    public void onFail(Exception ex);
    
}
