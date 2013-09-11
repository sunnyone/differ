package cz.nkp.differ.compare.metadata.external;

import cz.nkp.differ.compare.metadata.JP2Kernel;
import cz.nkp.differ.compare.metadata.JP2Metadata;
import cz.nkp.differ.compare.metadata.JP2Profile;
import cz.nkp.differ.compare.metadata.JP2ProfileValidationResult;
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
    
    @Autowired
    private JP2ProfileProvider jp2ProfileProvider;
    
    @Autowired
    private Jaxb2Marshaller marshaller;
    
    @Override
    public List<Entry> transform(byte[] stdout, byte[] stderr) throws IOException {
        List<Entry> entries = innerTransformer.transform(stdout, stderr);
        JP2Metadata metadata = getMetadata(entries);
        for (JP2Profile profile : jp2ProfileProvider.getProfiles()) {
            JP2ProfileValidationResult result = this.validate(metadata, profile);
        }
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
    
    private JP2ProfileValidationResult validate(JP2Metadata metadata, JP2Profile profile) {
        JP2ProfileValidationResult result = new JP2ProfileValidationResult();
        result.setProfile(profile);
        result.setMetadata(metadata);
        result.setKernel(profile.getKernel() == null || profile.getKernel().equals(metadata.getKernel()));
        result.setQualityLayers(profile.getMaxQualityLayers() < metadata.getQualityLayers() || 
                profile.getMinQualityLayers() > metadata.getQualityLayers());
        result.setProgressionOrder(validatePropertyAgainstList(profile.getProgressionOrders(), metadata.getProgressionOrder()));
        result.setDecompositionLevel(validatePropertyAgainstList(profile.getDecompositionLevels(), metadata.getDecompositionLevel()));
        result.setTileSize(validatePropertyAgainstList(profile.getTileSizes(), metadata.getTileSize()));
        if (result.isKernel() && result.isQualityLayers() && result.isProgressionOrder() 
                && result.isDecompositionLevel() && result.isTileSize()) {
            result.setValid(true);
        }
        return result;
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

    public JP2ProfileProvider getJp2ProfileProvider() {
        return jp2ProfileProvider;
    }

    public void setJp2ProfileProvider(JP2ProfileProvider jp2ProfileProvider) {
        this.jp2ProfileProvider = jp2ProfileProvider;
    }

    public Jaxb2Marshaller getMarshaller() {
        return marshaller;
    }

    public void setMarshaller(Jaxb2Marshaller marshaller) {
        this.marshaller = marshaller;
    }
    
    private static <T> boolean validatePropertyAgainstList(List<T> list, T value) {
        if (empty(list)) {
            return true;
        } else {
            return list.contains(value);
        }
    }
    
    private static <T> boolean empty(List<T> list) {
        return (list == null || list.isEmpty());
    }
    
}
