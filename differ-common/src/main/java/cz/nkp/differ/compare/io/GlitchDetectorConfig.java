package cz.nkp.differ.compare.io;

/**
 *
 * @author xrosecky
 */
public class GlitchDetectorConfig {

    private double maxAllowedRatioForAbsoluteRed = 0.6;

    private double maxAllowedRatioForAbsoluteGreen = 0.6;

    private double maxAllowedRatioForAbsoluteBlue = 0.6;

    public double getMaxAllowedRatioForAbsoluteBlue() {
	return maxAllowedRatioForAbsoluteBlue;
    }

    public void setMaxAllowedRatioForAbsoluteBlue(double maxAllowedRatioForAbsoluteBlue) {
	this.maxAllowedRatioForAbsoluteBlue = maxAllowedRatioForAbsoluteBlue;
    }

    public double getMaxAllowedRatioForAbsoluteGreen() {
	return maxAllowedRatioForAbsoluteGreen;
    }

    public void setMaxAllowedRatioForAbsoluteGreen(double maxAllowedRatioForAbsoluteGreen) {
	this.maxAllowedRatioForAbsoluteGreen = maxAllowedRatioForAbsoluteGreen;
    }

    public double getMaxAllowedRatioForAbsoluteRed() {
	return maxAllowedRatioForAbsoluteRed;
    }

    public void setMaxAllowedRatioForAbsoluteRed(double maxAllowedRatioForAbsoluteRed) {
	this.maxAllowedRatioForAbsoluteRed = maxAllowedRatioForAbsoluteRed;
    }

}
