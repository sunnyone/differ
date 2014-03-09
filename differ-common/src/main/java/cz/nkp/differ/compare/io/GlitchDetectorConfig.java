package cz.nkp.differ.compare.io;

/**
 * 
 * @author xrosecky
 */
public class GlitchDetectorConfig {

	private double maxAllowedRatioForAbsoluteRed = 60.0d;

	private double maxAllowedRatioForAbsoluteGreen = 60.0d;

	private double maxAllowedRatioForAbsoluteBlue = 60.0d;

	public double getMaxAllowedRatioForAbsoluteBlue() {
		return maxAllowedRatioForAbsoluteBlue;
	}

	public void setMaxAllowedRatioForAbsoluteBlue(
			double maxAllowedRatioForAbsoluteBlue) {
		this.maxAllowedRatioForAbsoluteBlue = maxAllowedRatioForAbsoluteBlue;
	}

	public double getMaxAllowedRatioForAbsoluteGreen() {
		return maxAllowedRatioForAbsoluteGreen;
	}

	public void setMaxAllowedRatioForAbsoluteGreen(
			double maxAllowedRatioForAbsoluteGreen) {
		this.maxAllowedRatioForAbsoluteGreen = maxAllowedRatioForAbsoluteGreen;
	}

	public double getMaxAllowedRatioForAbsoluteRed() {
		return maxAllowedRatioForAbsoluteRed;
	}

	public void setMaxAllowedRatioForAbsoluteRed(
			double maxAllowedRatioForAbsoluteRed) {
		this.maxAllowedRatioForAbsoluteRed = maxAllowedRatioForAbsoluteRed;
	}

}
