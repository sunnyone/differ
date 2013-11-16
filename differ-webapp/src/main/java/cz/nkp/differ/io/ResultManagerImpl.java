package cz.nkp.differ.io;

import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.compare.io.SerializableImageProcessorResults;
import cz.nkp.differ.dao.ResultDAO;
import cz.nkp.differ.model.Result;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author xrosecky
 * @author Thomas Truax
 */
@Transactional
public class ResultManagerImpl implements ResultManager {

    private static final String EXTENSION = ".xml";

    private String directory;

    private JAXBContext context;

    private Marshaller marshaller;

    private Unmarshaller unmarshaller;

    private ResultDAO resultDAO;

    public ResultManagerImpl() {
	init();
    }
    
    @Override
    public Result save(SerializableImageProcessorResults processorResult, String name) throws IOException {
	Result result = new Result();
	result.setName(name);
	result.setUserId(DifferApplication.getCurrentApplication().getLoggedUser().getId());
	resultDAO.persist(result);
	File outputFile = new File(directory, result.getId() + EXTENSION);
	OutputStream os;
	os = new FileOutputStream(outputFile);
	StreamResult streamResult = new StreamResult(os);
        try {
            marshaller.marshal(processorResult, streamResult);
        } catch (JAXBException ex) {
            Logger.getLogger(ResultManager.class.getName()).log(Level.SEVERE, null, ex);
        }
	return result;
    }

    @Override
    public List<Result> getResults() {
	List<Result> results = new ArrayList<Result>();
	if (directory != null) {
            File dir = new File(directory);
	    if (!dir.exists()) {
		if (!dir.mkdirs()) {
		    throw new RuntimeException(String.format("Directory %s can't be created.", dir.getAbsolutePath()));
		}
	    }
            for (File file : dir.listFiles()) {
                Result result = new Result();
                result.setName(file.getName());
                results.add(result);
            }
        }
	return results;
    }

    @Override
    public SerializableImageProcessorResults getResult(Result result) throws IOException {
	File input = new File(directory, result.getName());
	StreamSource source = new StreamSource(new FileInputStream(input));
        try {
            return (SerializableImageProcessorResults)unmarshaller.unmarshal(source);
        } catch (JAXBException ex) {
            Logger.getLogger(ResultManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getDirectory() {
	return directory;
    }

    public void setDirectory(String directory) {
	this.directory = directory;
    }

    public Marshaller getMarshaller() {
	return marshaller;
    }

    public void setMarshaller(Marshaller marshaller) {
	this.marshaller = marshaller;
    }
    
    public Unmarshaller getUnmarshaller() {
        return unmarshaller;
    }
    
    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }

    public ResultDAO getResultDAO() {
	return resultDAO;
    }

    public void setResultDAO(ResultDAO resultDAO) {
	this.resultDAO = resultDAO;
    }
    
    private void init() {
        try {
            context = JAXBContext.newInstance(SerializableImageProcessorResults.class);
            this.marshaller = context.createMarshaller();
            this.unmarshaller = context.createUnmarshaller();
        } catch (JAXBException ex) {
            Logger.getLogger(ResultManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
