package cz.nkp.differ.compare.task;

/**
 *
 * @author xrosecky
 */
public class StartRequest {
    
    private String identifier;
    
    private String filename1;
    
    private String filename2;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getFilename1() {
        return filename1;
    }

    public void setFilename1(String filename1) {
        this.filename1 = filename1;
    }

    public String getFilename2() {
        return filename2;
    }

    public void setFilename2(String filename2) {
        this.filename2 = filename2;
    }
    
}
