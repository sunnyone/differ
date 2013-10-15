package cz.nkp.differ.gui.components;


import org.apache.log4j.Logger;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import cz.nkp.differ.compare.io.CompareComponent;
import cz.nkp.differ.compare.io.ImageProcessorResult;
import cz.nkp.differ.listener.Message;
import cz.nkp.differ.listener.ProgressType;
import cz.nkp.differ.model.Image;
import java.text.SimpleDateFormat;

// TODO: rename
public class PluginDisplayComponent extends CustomComponent {

    private static final long serialVersionUID = -5172306282663506101L;
    private Logger LOGGER = Logger.getLogger(PluginDisplayComponent.class);

    public PluginDisplayComponent(CompareComponent d, Image[] images) {
	super();
	if (images == null) {
	    throw new NullPointerException("images");
	}
	this.setCompositionRoot(createPluginCompareComponent(d, images));

    }

    private Layout createPluginCompareComponent(CompareComponent d, Image[] images) {
	HorizontalLayout layout = new HorizontalLayout();
	layout.addComponent(new PluginDisplayPanel(d, images));
	return layout;
    }
}

// TODO: rename and move to separate class
class PluginDisplayPanel extends VerticalLayout implements WebProgressListener {

    private static final long serialVersionUID = -4597810967107465071L;
    private final ProgressIndicator progress = new ProgressIndicator();
    private final TextArea progressDetails = new TextArea();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
    private int numberOfTasks = 0;
    static Logger LOGGER = Logger.getLogger(PluginDisplayPanel.class);

    public PluginDisplayPanel(CompareComponent d, Image[] images) {
	d.addImages(images);
	d.setPluginDisplayComponentCallback(this);
	synchronized (progress) {
            progress.setWidth("300px");
	    progress.setIndeterminate(false);
	    progress.setImmediate(true);
	    progress.setPollingInterval(750);
	    progress.setCaption("Loading plugin...");
	    progress.setValue(0f);
	    this.addComponent(progress);
	}
        synchronized(progressDetails) {
            progressDetails.setWidth("300px");
            this.addComponent(progressDetails);
        }
    }

    @Override
    public void onStart(String identifier, int numberOfTasks) {
        this.numberOfTasks = numberOfTasks;
        synchronized (progressDetails) {
            progressDetails.setRows(numberOfTasks);
        }
    }

    @Override
    public void onProgress(Message message) {
        if (message.getProgressType() == ProgressType.FINISH) {
            int finished = message.getNumberOfFinishedTaks();
            float doneInPercent = finished / (numberOfTasks * 1.0f);
            String newLine = "\n";
            String text = dateFormat.format(message.getTime()) + 
                    String.format(" %s missing done", message.getEventType())
                    + newLine;
            synchronized (progressDetails) {
                progress.setValue(doneInPercent);
                if (((String) progressDetails.getValue()).isEmpty()) {
                    progressDetails.setValue(text);
                } else {
                    progressDetails.setValue(progressDetails.getValue() + text);
                }
            }
        }
    }

    @Override
    public void onFinish(String identifier, ImageProcessorResult[] results) {
        
    }

    @Override
    public void ready(Object c) {
        this.removeAllComponents();
	this.addComponent((Component) c);
    }
}
