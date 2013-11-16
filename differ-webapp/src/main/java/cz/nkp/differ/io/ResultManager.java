package cz.nkp.differ.io;

import cz.nkp.differ.compare.io.SerializableImageProcessorResults;
import cz.nkp.differ.model.Result;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public interface ResultManager {

    public SerializableImageProcessorResults getResult(Result result) throws IOException;

    public List<Result> getResults();

    public Result save(SerializableImageProcessorResults result, String name) throws IOException;
    
}
