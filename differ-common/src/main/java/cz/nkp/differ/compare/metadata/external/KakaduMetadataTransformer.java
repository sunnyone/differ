package cz.nkp.differ.compare.metadata.external;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public class KakaduMetadataTransformer implements ResultTransformer {

    private RegexpTransformer innerTransformer;
    
    @Override
    public List<Entry> transform(byte[] stdout, byte[] stderr) throws IOException {
        List<Entry> entries = innerTransformer.transform(stdout, stderr);
        return entries;
    }

    public RegexpTransformer getInnerTransformer() {
        return innerTransformer;
    }

    public void setInnerTransformer(RegexpTransformer innerTransformer) {
        this.innerTransformer = innerTransformer;
    }
    
}
