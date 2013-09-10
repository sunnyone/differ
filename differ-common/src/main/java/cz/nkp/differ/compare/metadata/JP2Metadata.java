package cz.nkp.differ.compare.metadata;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author xrosecky
 */
@XmlRootElement(name="profile")
@XmlType(propOrder = {"kernel", "preccintSizes", "tileSize", "decompositionLevel", "progressionOrder", "qualityLayers"})
@XmlAccessorType(XmlAccessType.FIELD)
public class JP2Metadata {

    @XmlElement(name = "kernel")
    private JP2Kernel kernel;
    
    @XmlElementWrapper(name = "preccints")
    @XmlElement(name = "preccint")
    private List<JP2Size> preccintSizes;
    
    @XmlElement(name = "tile")
    private JP2Size tileSize;
    
    @XmlElement(name = "decomposition-level")
    private int decompositionLevel;
    
    @XmlElement(name = "progression-order")
    private String progressionOrder;
    
    @XmlElement(name = "quality-layers")
    private int qualityLayers;

    public JP2Metadata() {
    }

    public JP2Kernel getKernel() {
        return kernel;
    }

    public void setKernel(JP2Kernel kernel) {
        this.kernel = kernel;
    }

    public List<JP2Size> getPreccintSizes() {
        return preccintSizes;
    }

    public void setPreccintSizes(List<JP2Size> preccintSizes) {
        this.preccintSizes = preccintSizes;
    }

    public JP2Size getTileSize() {
        return tileSize;
    }

    public void setTileSize(JP2Size tileSize) {
        this.tileSize = tileSize;
    }

    public int getDecompositionLevel() {
        return decompositionLevel;
    }

    public void setDecompositionLevel(int decompositionLevel) {
        this.decompositionLevel = decompositionLevel;
    }

    public String getProgressionOrder() {
        return progressionOrder;
    }

    public void setProgressionOrder(String progressionOrder) {
        this.progressionOrder = progressionOrder;
    }

    public int getQualityLayers() {
        return qualityLayers;
    }

    public void setQualityLayers(int qualityLayers) {
        this.qualityLayers = qualityLayers;
    }

}
