package cz.nkp.differ.compare.metadata;

/**
 *
 * @author xrosecky
 */
public class JP2ProfileValidationResult {
    
    public JP2Metadata metadata;
    
    public JP2Profile profile;
    
    public boolean valid;
    
    public boolean kernel;
    
    public boolean preccintSizes;
    
    public boolean tileSize;
    
    public boolean decompositionLevel;
    
    public boolean progressionOrder;
    
    public boolean qualityLayers;

    public JP2ProfileValidationResult() {
    } 

    public JP2Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(JP2Metadata metadata) {
        this.metadata = metadata;
    }

    public JP2Profile getProfile() {
        return profile;
    }

    public void setProfile(JP2Profile profile) {
        this.profile = profile;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
    
    public boolean isKernel() {
        return kernel;
    }

    public void setKernel(boolean kernel) {
        this.kernel = kernel;
    }

    public boolean isPreccintSizes() {
        return preccintSizes;
    }

    public void setPreccintSizes(boolean preccintSizes) {
        this.preccintSizes = preccintSizes;
    }

    public boolean isTileSize() {
        return tileSize;
    }

    public void setTileSize(boolean tileSize) {
        this.tileSize = tileSize;
    }

    public boolean isDecompositionLevel() {
        return decompositionLevel;
    }

    public void setDecompositionLevel(boolean decompositionLevel) {
        this.decompositionLevel = decompositionLevel;
    }

    public boolean isProgressionOrder() {
        return progressionOrder;
    }

    public void setProgressionOrder(boolean progressionOrder) {
        this.progressionOrder = progressionOrder;
    }

    public boolean isQualityLayers() {
        return qualityLayers;
    }

    public void setQualityLayers(boolean qualityLayers) {
        this.qualityLayers = qualityLayers;
    }
    
}
