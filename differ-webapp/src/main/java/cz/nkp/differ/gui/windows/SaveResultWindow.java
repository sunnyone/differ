package cz.nkp.differ.gui.windows;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.compare.io.ImageProcessorResult;
import cz.nkp.differ.util.GUIMacros;
import java.io.IOException;

/**
 *
 * @author xrosecky
 */
public class SaveResultWindow extends CustomWindow {

    private ImageProcessorResult[] results;

    private TextField nameField;
    private CheckBox sharedCheckBox;

    private class SaveButtonClickListener implements ClickListener {

	@Override
	public void buttonClick(Button.ClickEvent event) {
	    String name = (String) nameField.getValue();
	    Boolean shared = sharedCheckBox.booleanValue();
	    try {
		DifferApplication.getResultManager().save(results, name, shared);
		DifferApplication.getMainApplicationWindow().showNotification("Success",
                    "<br/>Results XML has been exported successfully," +
                    "<br/>they can be found in the Results tab",
                    Window.Notification.TYPE_HUMANIZED_MESSAGE);
	    } catch (IOException ioe) {
		DifferApplication.getMainApplicationWindow().showNotification("Failed",
                    "<br/>Saving of result failed",
                    Window.Notification.TYPE_ERROR_MESSAGE);
	    }
	}
    }

    public SaveResultWindow(ImageProcessorResult[] results) {
	super("Save result");
	this.results = results;
	addComponent(createResultDialog());

	HorizontalLayout buttonLayout = new HorizontalLayout();

	Button login = new Button("Save");
	login.setClickShortcut(KeyCode.ENTER);
	login.addListener(new SaveButtonClickListener());
	buttonLayout.addComponent(login);

	Button close = new Button("Close");
	close.addListener(GUIMacros.createWindowCloseButtonListener(this));
	buttonLayout.addComponent(close);

	addComponent(buttonLayout);
    }

    private Layout createResultDialog() {
	VerticalLayout layout = new VerticalLayout();
	nameField = new TextField("Name");
	layout.addComponent(nameField);
	sharedCheckBox = new CheckBox("Shared");
	layout.addComponent(sharedCheckBox);
	return layout;
    }
}
