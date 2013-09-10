package cz.nkp.differ.compare.metadata.external;

import cz.nkp.differ.compare.metadata.JP2Kernel;
import cz.nkp.differ.compare.metadata.JP2Metadata;
import cz.nkp.differ.compare.metadata.JP2Size;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

/**
 *
 * @author xrosecky
 */
public class KakaduMetadataTransformer implements ResultTransformer {
    
    private static final Pattern KDU_LIST_SEPARATOR = Pattern.compile("\\},\\{");
    private static final Pattern KDU_RANGE_PATTERN = Pattern.compile("\\{{0,1}([0-9]+),([0-9]+)\\}{0,1}");

    private RegexpTransformer innerTransformer;
    
    private JP2ProfileProvider jp2ProfileProvider;
    
    @Autowired
    private Jaxb2Marshaller marshaller;
    
    @Override
    public List<Entry> transform(byte[] stdout, byte[] stderr) throws IOException {
        List<Entry> entries = innerTransformer.transform(stdout, stderr);
        JP2Metadata metadata = getMetadata(entries);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Result result = new StreamResult(bos);
        marshaller.marshal(metadata, result);
        System.out.println(new String(bos.toByteArray()));
        return entries;
    }
    
    private JP2Metadata getMetadata(List<Entry> entries) {
        JP2Metadata metadata = new JP2Metadata();
        Map<String, String> map = new HashMap<String, String>();
        for (Entry entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }
        
        String kernel = map.get("Type of algorithm");
        if ("W5X3".equals(kernel)) {
            metadata.setKernel(JP2Kernel.Revesible5x3);
        } else if ("W9X7".equals(kernel)) {
            metadata.setKernel(JP2Kernel.Irreversible9x7);
        } else {
            metadata.setKernel(null);
        }
        
        String tile = map.get("Tiles");
        if (tile != null) {
            metadata.setTileSize(parseRange(tile));
        }
        
        String levels = map.get("Number of decomposition levels");
        if (levels != null) {
            metadata.setDecompositionLevel(Integer.parseInt(levels));
        }
        
        String qualityLayers = map.get("Number of quality layers");
        if (levels != null) {
            metadata.setQualityLayers(Integer.parseInt(qualityLayers));
        }
        
        String progressionOrder = map.get("Progression order");
        if (progressionOrder != null) {
            metadata.setProgressionOrder(progressionOrder);
        }
        
        String preccints = map.get("Preccints size");
        ArrayList<JP2Size> preccintsList = new ArrayList<JP2Size>();
        if (preccints != null) {
            for (String preccint : KDU_LIST_SEPARATOR.split(preccints)) {
                preccintsList.add(parseRange(preccint));
            }
        }
        metadata.setPreccintSizes(preccintsList);
        
        return metadata;
    }
    
    private JP2Size parseRange(String value) {
        Matcher matcher = KDU_RANGE_PATTERN.matcher(value);
        boolean matchFound = matcher.find();
	if (matchFound && matcher.groupCount() == 2) {
            String width = matcher.group(1).trim();
            String height = matcher.group(2).trim();
            JP2Size size = new JP2Size();
            size.setWidth(Integer.parseInt(width));
            size.setHeight(Integer.parseInt(height));
            return size;
        } else {
            throw new IllegalArgumentException(value);
        }
    }

    public RegexpTransformer getInnerTransformer() {
        return innerTransformer;
    }

    public void setInnerTransformer(RegexpTransformer innerTransformer) {
        this.innerTransformer = innerTransformer;
    }
    
}
