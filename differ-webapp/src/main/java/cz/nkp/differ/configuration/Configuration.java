package cz.nkp.differ.configuration;

/**
 *
 * @author xrosecky
 */
public class Configuration {
 
    private boolean captchaEnabled = true;
    
    private GoogleAnalyticsConfiguration googleAnalyticsConfiguration;

    public boolean isCaptchaEnabled() {
        return captchaEnabled;
    }

    public void setCaptchaEnabled(boolean captchaEnabled) {
        this.captchaEnabled = captchaEnabled;
    }

    public GoogleAnalyticsConfiguration getGoogleAnalyticsConfiguration() {
        return googleAnalyticsConfiguration;
    }

    public void setGoogleAnalyticsConfiguration(GoogleAnalyticsConfiguration googleAnalyticsConfiguration) {
        this.googleAnalyticsConfiguration = googleAnalyticsConfiguration;
    }
    
}
