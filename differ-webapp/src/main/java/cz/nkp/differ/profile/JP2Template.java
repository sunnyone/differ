package cz.nkp.differ.profile;

import cz.nkp.differ.compare.metadata.JP2Kernel;
import cz.nkp.differ.compare.metadata.JP2Size;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public interface JP2Template {
    
    public List<JP2Kernel> getKernels();
    
    public List<JP2Size> getPreccintSizes();
    
    public List<JP2Size> getTileSizes();
    
    public List<String> getProgressionOrders();
    
    public List<Integer> getDecompositionLevels();
    
}
