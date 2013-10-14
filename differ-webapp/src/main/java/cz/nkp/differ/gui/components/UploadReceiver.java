package cz.nkp.differ.gui.components;

import com.vaadin.ui.Upload.Receiver;
import cz.nkp.differ.DifferApplication;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Thomas Truax
 */
public class UploadReceiver implements Receiver {
    
    private static final String filePrefix = "differ_webapp";
    
    private File file;
    
    public UploadReceiver() {
    }
 
    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {           
           try {
               String fileSuffix = "." + getExtension(filename);
               file = File.createTempFile(filePrefix, fileSuffix);
               return new FileOutputStream(file);
           } catch (IOException ioe) {
               DifferApplication.getCurrentApplication().getMainWindow().showNotification("Error", "<br/>Error while uploading file");
               return null;
           }
    }

    public File getFile() {
        return file;
    }
    
    private static String getExtension(String fileName) {
	String extension = "";
	int dotAt = fileName.lastIndexOf('.');
	if (dotAt != -1) {
	    extension = fileName.substring(dotAt + 1);
	}
	return extension;
    }

    
}
