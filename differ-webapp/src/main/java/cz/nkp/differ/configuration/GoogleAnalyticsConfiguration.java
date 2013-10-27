package cz.nkp.differ.configuration;

/**
 *
 * @author xrosecky
 */
public class GoogleAnalyticsConfiguration {
    
    private boolean enabled = true;
    
    private String domainName;
    
    private String trackerId;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getTrackerId() {
        return trackerId;
    }

    public void setTrackerId(String trackerId) {
        this.trackerId = trackerId;
    }
    
}
