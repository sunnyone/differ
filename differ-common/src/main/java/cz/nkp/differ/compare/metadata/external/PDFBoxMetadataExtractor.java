/**
 * 
 */
package cz.nkp.differ.compare.metadata.external;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.activation.FileDataSource;

import org.apache.pdfbox.Version;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.preflight.PreflightDocument;
import org.apache.pdfbox.preflight.ValidationResult;
import org.apache.pdfbox.preflight.ValidationResult.ValidationError;
import org.apache.pdfbox.preflight.exception.SyntaxValidationException;
import org.apache.pdfbox.preflight.parser.PreflightParser;

import cz.nkp.differ.compare.metadata.AbstractMetadataExtractor;
import cz.nkp.differ.compare.metadata.ImageMetadata;
import cz.nkp.differ.compare.metadata.MetadataSource;

/**
 * @author Jan Stavěl
 *
 */
public class PDFBoxMetadataExtractor extends AbstractMetadataExtractor {
    private String source;
    private Map<String, String> units;

	public void setSource(String source){
        this.source = source;
    }
	
	
	/* (non-Javadoc)
	 * @see cz.nkp.differ.compare.metadata.MetadataExtractor#getSource()
	 */
	@Override
	public String getSource() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see cz.nkp.differ.compare.metadata.MetadataExtractor#getMetadata(java.io.File)
	 */
	@Override
	public List<ImageMetadata> getMetadata(File imageFile) {
		// TODO Auto-generated method stub
	    if ( !super.isSupported(imageFile)) {
	            return Collections.emptyList();
	    };
	    List<ImageMetadata> result = new ArrayList<ImageMetadata>();
        MetadataSource metadataSource = new MetadataSource(0, new String(""), new String(""), this.source);
        result.add(new ImageMetadata("File name", imageFile.getName(), metadataSource));
        result.add(new ImageMetadata("exit-code", "OK", metadataSource));
        result.add(new ImageMetadata("Version of Extractor", getExtractorVersion(), metadataSource));
        result.add(new ImageMetadata("Conformance with PDF/A", this.isValidPDFA(imageFile),metadataSource));
        
        PDDocument document = this.getPDDocument(imageFile);
        PDDocumentInformation info = document.getDocumentInformation();
        result.add(new ImageMetadata("Author", info.getAuthor(), metadataSource));
        result.add(new ImageMetadata("Title", info.getTitle(), metadataSource));
        result.add(new ImageMetadata("Page Count", document.getNumberOfPages(), metadataSource));
        result.add(new ImageMetadata("Subject",  info.getSubject(), metadataSource));
        result.add(new ImageMetadata("Creator",  info.getCreator(), metadataSource));
        result.add(new ImageMetadata("Producer", info.getProducer(), metadataSource));
        result.add(new ImageMetadata("Keywords", info.getKeywords(), metadataSource));
        result.add(new ImageMetadata("Trapped PDF", info.getTrapped(), metadataSource));
        
        //to read the XML metadata
        try {
        	PDDocumentCatalog catalog = document.getDocumentCatalog();
            PDMetadata metadata = catalog.getMetadata();
            if( metadata != null){
            	//InputStream xmlInputStream = metadata.createInputStream();
            	byte[] xmp = metadata.getByteArray();
            	//OutputStream out=metadata.exportXMPMetadata();
            	result.add(new ImageMetadata("XMP",new String(xmp), metadataSource));
            }
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e){
			e.printStackTrace();
		}

        /*
        try {
        
        	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss+zzz");
			result.add(new ImageMetadata("File created", dateFormat.format(info.getCreationDate()), metadataSource));
	        result.add(new ImageMetadata("File last modified", dateFormat.format(info.getModificationDate()), metadataSource));
	        result.add(new ImageMetadata("File Permissions", document.getCurrentAccessPermission().toString(), metadataSource));
        } catch (IOException e) {
			e.printStackTrace();
		}
		*/
        return result;
	}
	
    public PDDocument getPDDocument(File imageFile){
    	PDDocument doc=null;
		try {
			doc = PDDocument.load(imageFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return doc;
    }
	public Boolean isValidPDFA(File imageFile){
		// return if the file is PDF/A-1B complaint
		ValidationResult result = null;
		FileDataSource fd = new FileDataSource(imageFile);
		try {
			PreflightParser parser = new PreflightParser(fd);

		  /* Parse the PDF file with PreflightParser that inherits from the NonSequentialParser.
		   * Some additional controls are present to check a set of PDF/A requirements. 
		   * (Stream length consistency, EOL after some Keyword...)
		   */
		  parser.parse();

		  /* Once the syntax validation is done, 
		   * the parser can provide a PreflightDocument 
		   * (that inherits from PDDocument) 
		   * This document process the end of PDF/A validation.
		   */
		  PreflightDocument document = parser.getPreflightDocument();
		  document.validate();

		  // Get validation result
		  result = document.getResult();
		  document.close();

		} catch (SyntaxValidationException e) {
		  /* the parse method can throw a SyntaxValidationException 
		   * if the PDF file can't be parsed.
		   * In this case, the exception contains an instance of ValidationResult  */
		  result = e.getResult();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

		// display validation result
		if (result.isValid()) {
			return true;
		} else {
			for (ValidationError error : result.getErrorsList()) {
				System.out.println(error.getErrorCode() + " : " + error.getDetails());
			}
		}
		return false;
	}

    public String getExtractorVersion(){
    	return Version.getVersion();
    }
	public Map<String, String> getUnits() {
		return units;
	}

	public void setUnits(Map<String, String> units) {
		this.units = units;
	}
}
