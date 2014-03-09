package cz.nkp.differ.gui.windows;

import com.vaadin.data.validator.DoubleValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Form;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import cz.nkp.differ.compare.io.GlitchDetectorConfig;

/**
 *
 * @author xrosecky
 */
public class GlitchDetectorWindow extends Window {

    private Form form = null;
    private VerticalLayout windowLayout;
    private GlitchDetectorConfig config;
    private ClickListener onSubmit;

    public GlitchDetectorWindow(GlitchDetectorConfig config) {
	this.config = config;
    }

    public void setOnSubmit(ClickListener onSubmit) {
	this.onSubmit = onSubmit;
    }

    public void init() {
	setCaption("Glitch detector setting");
	setModal(true);
	setDraggable(false);
	setResizable(false);
	center();
	setWidth("33%");
	windowLayout = new VerticalLayout();
	windowLayout.setSpacing(true);
	createForm();
	windowLayout.addComponent(form);
	Button apply = new Button("Apply");
	apply.addListener(new ClickListener() {

	    @Override
	    public void buttonClick(ClickEvent event) {
		boolean valid = GlitchDetectorWindow.this.updateGlitchDetectorConfig();
		if (valid) {
		    onSubmit.buttonClick(event);
		    GlitchDetectorWindow.this.close();
		}
	    }
	});
        windowLayout.addComponent(apply);
	addComponent(windowLayout);
    }

    private void createForm() {
	form = new Form();
	TextField maxRedRatio = new TextField("Max absolute red ratio (in %)");
        maxRedRatio.setValue(Double.toString(config.getMaxAllowedRatioForAbsoluteRed()));
        maxRedRatio.addValidator(new DoubleValidator("Value must be double"));
        form.addField("maxAllowedRatioForAbsoluteRed", maxRedRatio);

	TextField maxGreenRatio = new TextField("Max absolute green ratio (in %)");
        maxGreenRatio.setValue(Double.toString(config.getMaxAllowedRatioForAbsoluteGreen()));
        maxGreenRatio.addValidator(new DoubleValidator("Value must be double"));
        form.addField("maxAllowedRatioForAbsoluteGreen", maxGreenRatio);

	TextField maxBlueRatio = new TextField("Max absolute blue ratio (in %)");
        maxBlueRatio.setValue(Double.toString(config.getMaxAllowedRatioForAbsoluteBlue()));
        maxBlueRatio.addValidator(new DoubleValidator("Value must be double"));
        form.addField("maxAllowedRatioForAbsoluteBlue", maxBlueRatio);
    }

    public boolean updateGlitchDetectorConfig() {
	boolean valid = form.isValid();
	if (valid) {
	    double maxAllowedRatioForAbsoluteRed = Double.parseDouble((String) form.getField("maxAllowedRatioForAbsoluteRed").getValue());
	    double maxAllowedRatioForAbsoluteGreen = Double.parseDouble((String) form.getField("maxAllowedRatioForAbsoluteGreen").getValue());
	    double maxAllowedRatioForAbsoluteBlue = Double.parseDouble((String) form.getField("maxAllowedRatioForAbsoluteBlue").getValue());
	    config.setMaxAllowedRatioForAbsoluteRed(maxAllowedRatioForAbsoluteRed);
	    config.setMaxAllowedRatioForAbsoluteGreen(maxAllowedRatioForAbsoluteGreen);
	    config.setMaxAllowedRatioForAbsoluteBlue(maxAllowedRatioForAbsoluteBlue);
	}
	return valid;
    }

}
