package cz.nkp.differ.tools;

/**
 *
 * @author xrosecky
 */
public class ExecutionTime {

    private long start = System.currentTimeMillis();
    
    public ExecutionTime() {
    }
    
    public void reset() {
        this.start = System.currentTimeMillis();
    }
    
    public long getTime() {
        long now = System.currentTimeMillis();
        return (now - start);
    }
    
}
