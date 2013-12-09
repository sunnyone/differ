package cz.nkp.differ.io;

import cz.nkp.differ.compare.io.ImageProcessorResult;
import cz.nkp.differ.compare.io.SerializableImageProcessorResults;
import cz.nkp.differ.model.Result;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public interface ResultManager {

    public SerializableImageProcessorResults getResult(Result result) throws IOException;
    
    public void delete(Result result);
    
    public void update(Result result);

    public List<Result> getResults();
    
    public List<Result> getSharedResults();

    public Result save(SerializableImageProcessorResults result, String name, boolean shared) throws IOException;

    public Result save(ImageProcessorResult[] results, String name, boolean shared) throws IOException;
    
    public void save(ImageProcessorResult[] results, OutputStream os);
    
}
