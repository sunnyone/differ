package cz.nkp.differ.io;

import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.compare.io.ImageProcessorResult;
import cz.nkp.differ.compare.io.SerializableImage;
import cz.nkp.differ.compare.io.SerializableImageProcessorResult;
import cz.nkp.differ.compare.io.SerializableImageProcessorResults;
import cz.nkp.differ.compare.metadata.ImageMetadata;
import cz.nkp.differ.compare.metadata.JP2Metadata;
import cz.nkp.differ.compare.metadata.JP2Profile;
import cz.nkp.differ.compare.metadata.MetadataSource;
import cz.nkp.differ.compare.metadata.ValidatedProperty;
import cz.nkp.differ.dao.ResultDAO;
import cz.nkp.differ.model.Result;
import cz.nkp.differ.model.User;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author xrosecky
 * @author Thomas Truax
 */
@Transactional
public class ResultManagerImpl implements ResultManager, InitializingBean {

    private static final String EXTENSION = ".xml";

    private String directory;

    private JAXBContext context;

    private Marshaller marshaller;

    private Unmarshaller unmarshaller;

    private ResultDAO resultDAO;

    private boolean syncWithFilesystem = true;

    private boolean saveFullImage = false;

    @Override
    public Result save(ImageProcessorResult[] results, String name, boolean shared) throws IOException {
	ArrayList<SerializableImageProcessorResult> resultsList = new ArrayList<SerializableImageProcessorResult>();
        for (ImageProcessorResult result : results) {
            SerializableImageProcessorResult res = new SerializableImageProcessorResult();
            if (result != null) {
                try {
                    if (saveFullImage && result.getFullImage() != null) {
                        res.setFullImage(new SerializableImage(result.getFullImage()));
                    }
                    if (result.getPreview() != null) {
                        res.setPreview(new SerializableImage(result.getPreview()));
                    }
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(ResultManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                res.setChecksum(result.getMD5Checksum());
                res.setHistogram(result.getHistogram());
                res.setType(result.getType());
                res.setWidth(result.getWidth());
                res.setHeight(result.getHeight());
                res.setMetadata(result.getMetadata());
                Set<MetadataSource> sourcesSet = new HashSet<MetadataSource>();
                for (ImageMetadata data : result.getMetadata()) {
                    if (!sourcesSet.contains(data.getSource())) {
                        sourcesSet.add(data.getSource());
                    }
                }
                List<MetadataSource> sources = new ArrayList<MetadataSource>();
                sources.addAll(sourcesSet);
                res.setSources(sources);
            }
            resultsList.add(res);
        }
        SerializableImageProcessorResults sipr = new SerializableImageProcessorResults();
        sipr.setResults(resultsList);
	return save(sipr, name, shared);
    }
    
    @Override
    public void delete(Result result) {
        resultDAO.delete(result);
    }
    
    @Override
    public void update(Result result) {
        result = resultDAO.merge(result);
        resultDAO.persist(result);
    }

    @Override
    public Result save(SerializableImageProcessorResults processorResult, String name, boolean shared) throws IOException {
	Result result = new Result();
	result.setName(name);
	result.setShared(shared);
	result.setUserId(DifferApplication.getCurrentApplication().getLoggedUser().getId());
	resultDAO.persist(result);
	OutputStream os = new FileOutputStream(getFile(result));
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
	User loggedUser = DifferApplication.getCurrentApplication().getLoggedUser();
	if (loggedUser == null) {
	    throw new RuntimeException("User is not logged in!");
        }
        return resultDAO.findByUser(loggedUser);
    }
    
    @Override
    public List<Result> getSharedResults() {
        return resultDAO.findAllShared(); 
    }
    

    @Override
    public SerializableImageProcessorResults getResult(Result result) throws IOException {
	File input = getFile(result);
	StreamSource source = new StreamSource(new FileInputStream(input));
        try {
            return (SerializableImageProcessorResults) unmarshaller.unmarshal(source);
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

    public boolean isSyncWithFilesystem() {
	return syncWithFilesystem;
    }

    public void setSyncWithFilesystem(boolean syncWithFilesystem) {
	this.syncWithFilesystem = syncWithFilesystem;
    }

    public boolean isSaveFullImage() {
	return saveFullImage;
    }

    public void setSaveFullImage(boolean saveFullImage) {
	this.saveFullImage = saveFullImage;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
	context = JAXBContext.newInstance(SerializableImageProcessorResults.class,
                ValidatedProperty.class,
                JP2Metadata.class,
                JP2Profile.class,
                SerializableImageProcessorResult.class
        );
	marshaller = context.createMarshaller();
	unmarshaller = context.createUnmarshaller();
	File dir = new File(directory);
	if (!dir.exists()) {
	    if (!dir.mkdirs()) {
		throw new RuntimeException(String.format("Directory %s can't be created.", dir.getAbsolutePath()));
	    }
	}
	if (syncWithFilesystem) {
	    doSyncWithFilesystem();
	}
    }

    private void doSyncWithFilesystem() {
	for (Result result : resultDAO.findAll()) {
	    File file = getFile(result);
	    if (!file.exists()) {
		resultDAO.delete(result);
	    }
	}
    }

    private File getFile(Result result) {
	String fileName = Long.toString(result.getId()) + EXTENSION;
	return new File(directory, fileName);
    }

}
