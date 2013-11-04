package cz.nkp.differ.compare.io;

import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.compare.io.generators.ImageMetadataComponentGenerator;
import cz.nkp.differ.listener.ProgressListener;
import cz.nkp.differ.model.Image;
import cz.nkp.differ.plugins.tools.PluginPollingThread;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import org.apache.log4j.Logger;


public class CompareComponent {
    
    public static final int ONE_ROW_WIDTH = 400;
    
    public static final int TWO_ROW_WIDTH = 400;

    public static Logger LOGGER = Logger.getRootLogger();
    private Application application = null;
    private PluginPollingThread currentThread;
    private Image[] images;
    private ImageProcessorResult[] results;
    
    public String getName() {
	return "Compare";
    }

    public void showSeriousError(String message) {
	Window.Notification errorNotif = new Window.Notification("Plugin Error",
		"A runtime error has occured while executing a plugin. Plugin operation halted. Message: " + message,
		Window.Notification.TYPE_ERROR_MESSAGE);

	application.getMainWindow().showNotification(errorNotif);
    }

    public void setImages(Image[] images) {
	this.images = images;
    }

    public void setPluginDisplayComponentCallback(final ProgressListener listener) {
	try {
	    currentThread = new PluginPollingThread(this, listener);
            DifferApplication.getCurrentApplication().invokeLater(currentThread);
	} catch (Exception e) {
	    showSeriousError(e.getLocalizedMessage());
	}
    }
    
    public void waitForResults(ProgressListener listener) {
        ImageProcessor imageProcessor = (ImageProcessor) DifferApplication.getApplicationContext().getBean("imageProcessor");
        if (images.length == 2) {
            try {
                results = imageProcessor.processImages(images[0].getFile(), images[1].getFile(), listener);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            results = new ImageProcessorResult[images.length];
            for (int i = 0; i < images.length; i++) {
                try {
                    results[i] = imageProcessor.processImage(images[i].getFile(), listener);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    public Component getPluginDisplayComponent() {
	if (images.length == 2) {
            
            GridLayout grid = new GridLayout(3, 3);
            
            ImageFileAnalysisContainer iFAC1 = new ImageFileAnalysisContainer(results[0], this, 0, images[0].getFileName());
            grid.addComponent(iFAC1.getComponent(), 0, 0);
            
            ImageFileAnalysisContainer iFAC2 = new ImageFileAnalysisContainer(results[1], this, 1, images[1].getFileName());
            grid.addComponent(iFAC2.getComponent(), 1, 0);
            
            ImageProcessorResult[] resultsForMetadata = new ImageProcessorResult[] {results[0], results[1]};
            ImageMetadataComponentGenerator table = new ImageMetadataComponentGenerator(resultsForMetadata, this);
            Component metadataTable = table.getComponent();
            grid.addComponent(metadataTable, 0, 1, 1, 1);
            
            if (results[2] != null) {
                Label comparedChecksum;
		if (iFAC1.getChecksum().equals(iFAC2.getChecksum())) {
                    comparedChecksum = new Label("Image hash values are equal.", Label.CONTENT_XHTML);
                } else {
                    comparedChecksum = new Label("Image hash values are NOT equal.", Label.CONTENT_XHTML);
                }
                ImageFileAnalysisContainer iFAC3 = new ImageFileAnalysisContainer(results[2], this, 2);
                iFAC3.setChecksumLabel(comparedChecksum);
                grid.addComponent(iFAC3.getComponent(), 2, 0);
                ImageMetadataComponentGenerator tableComp = new ImageMetadataComponentGenerator(new ImageProcessorResult[] {results[2]}, this);
                tableComp.setTableName("SIMILARITY METRICS");
                grid.addComponent(tableComp.getComponent(), 2, 1);
	    } else {
		Label errorComponent = new Label("Images can't be compared.");
		grid.addComponent(errorComponent, 2, 0);
	    }
            return grid;
	} else {
            VerticalLayout layout = new VerticalLayout();
	    HorizontalLayout childLayout = new HorizontalLayout();
            for (int i = 0; i < images.length; i++) {
		try {
		    ImageFileAnalysisContainer iFAC = new ImageFileAnalysisContainer(results[i], this, i, images[i].getFileName());
		    childLayout.addComponent(iFAC.getComponent());
		} catch (Exception ex) {
		    ex.printStackTrace();
		    throw new RuntimeException(ex);
		}
	    }
            layout.addComponent(childLayout);
            ImageMetadataComponentGenerator table = new ImageMetadataComponentGenerator(results, this);
            layout.addComponent(table.getComponent());
            layout.addComponent(addExportResultsButton(results));
	    return layout;
	}
    }
    
    private Component addExportResultsButton(final ImageProcessorResult[] ipr) {
        Button btnSave = new Button("Save Results");
        btnSave.addListener(new ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                exportResultsToXml(ipr);
                DifferApplication.getMainApplicationWindow().showNotification("Success", 
                    "<br/>Results XML has been exported successfully," +
                    "<br/>they can be found in the Results tab", 
                    Window.Notification.TYPE_HUMANIZED_MESSAGE);                
            }   
        });
        return btnSave;
    }
    
    private void exportResultsToXml(ImageProcessorResult[] ipr) {
        ArrayList<SerializableImageProcessorResult> resultsList = new ArrayList<SerializableImageProcessorResult>();
        for (ImageProcessorResult result : ipr) {
            SerializableImageProcessorResult res = new SerializableImageProcessorResult();
            try {
                if (result.getFullImage() != null) {
                    res.setFullImage(new SerializableImage(result.getFullImage()));
                }
                if (result.getPreview() != null) {
                    res.setPreview(new SerializableImage(result.getPreview()));
                } 
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(CompareComponent.class.getName()).log(Level.SEVERE, null, ex);
            }
            res.setHistogram(result.getHistogram());
            res.setType(result.getType());
            res.setWidth(result.getWidth());
            res.setHeight(result.getHeight());
            res.setMetadata(result.getMetadata());
            resultsList.add(res);
        }
        SerializableImageProcessorResults sipr = new SerializableImageProcessorResults();
        sipr.setResults(resultsList);
        //FIXME: hardcoded
        /*
        ResultManager resultMan = DifferApplication.getResultManager();
        String resultsDir = "/tmp/differ/" + DifferApplication.getUserManager().getLoggedInUser() + "/results";
        new File(resultsDir).mkdirs(); //make path in case it don't exist
        resultMan.setDirectory(resultsDir);
        resultMan.createJAXBContext(SerializableImageProcessorResults.class);
        try {
            resultMan.save(sipr);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(CompareComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
    }
}
