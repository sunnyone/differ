package cz.nkp.differ.compare.metadata;

import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author xrosecky
 */
public class JP2Metadata {

    @XmlElement(name = "kernel")
    private JP2Kernel kernel;
    
    @XmlElement(name = "preccint")
    private JP2Size preccintSize;
    
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

    public JP2Size getPreccintSize() {
        return preccintSize;
    }

    public void setPreccintSize(JP2Size preccintSize) {
        this.preccintSize = preccintSize;
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
