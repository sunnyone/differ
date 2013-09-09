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
@XmlType(propOrder = {"kernel", "preccintSizes", "tileSizes", "decompositionLevels", "progressionOrders", "minQualityLayers", "maxQualityLayers"})
@XmlAccessorType(XmlAccessType.FIELD)
public class JP2Profile {

    @XmlAccessorType(XmlAccessType.FIELD)
    public static enum Kernel {
        Revesible5x3,
        Irreversible9x7
    }
    
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Size {
        
        public int width;
        
        public int height;
        
        public Size() {
            
        }

        public Size(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
        
    }
    
    public JP2Profile() {
    }

    @XmlElement(name = "kernel")
    private Kernel kernel;
    
    @XmlElementWrapper(name = "preccints")
    @XmlElement(name = "preccint")
    private List<Size> preccintSizes;
    
    @XmlElementWrapper(name = "tiles")
    @XmlElement(name = "tile")
    private List<Size> tileSizes;
    
    @XmlElementWrapper(name = "decomposition-levels")
    @XmlElement(name = "decomposition-level")
    private List<Integer> decompositionLevels;
    
    @XmlElementWrapper(name = "progression-orders")
    @XmlElement(name = "progression-order")
    private List<String> progressionOrders;
    
    @XmlElement(name = "min-quality-layers")
    private int minQualityLayers;
    
    @XmlElement(name = "max-quality-layers")
    private int maxQualityLayers;

    public Kernel getKernel() {
        return kernel;
    }

    public void setKernel(Kernel kernel) {
        this.kernel = kernel;
    }

    public List<Size> getPreccintSizes() {
        return preccintSizes;
    }

    public void setPreccintSizes(List<Size> preccintSizes) {
        this.preccintSizes = preccintSizes;
    }

    public List<Size> getTileSizes() {
        return tileSizes;
    }

    public void setTileSizes(List<Size> tileSizes) {
        this.tileSizes = tileSizes;
    }

    public List<Integer> getDecompositionLevels() {
        return decompositionLevels;
    }

    public void setDecompositionLevels(List<Integer> decompositionLevels) {
        this.decompositionLevels = decompositionLevels;
    }

    public List<String> getProgressionOrders() {
        return progressionOrders;
    }

    public void setProgressionOrders(List<String> progressionOrders) {
        this.progressionOrders = progressionOrders;
    }

    public int getMinQualityLayers() {
        return minQualityLayers;
    }

    public void setMinQualityLayers(int minQualityLayers) {
        this.minQualityLayers = minQualityLayers;
    }

    public int getMaxQualityLayers() {
        return maxQualityLayers;
    }

    public void setMaxQualityLayers(int maxQualityLayers) {
        this.maxQualityLayers = maxQualityLayers;
    }
    
}
