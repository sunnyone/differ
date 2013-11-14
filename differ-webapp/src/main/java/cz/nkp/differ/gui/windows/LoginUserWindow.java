package cz.nkp.differ.gui.windows;

import com.vaadin.data.validator.NullValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.gui.components.ProjectHeaderPanel;
import cz.nkp.differ.gui.tabs.DifferProgramTab;
import cz.nkp.differ.util.GUIMacros;

/**
 *
 * @author Thomas Truax
 */
public class LoginUserWindow extends CustomWindow {

    DifferProgramTab appBody;
    ProjectHeaderPanel appHeader;
    TextField nameField;
    PasswordField passField;

    private class LoginButtonClickListener implements ClickListener {

        @Override
        public void buttonClick(Button.ClickEvent event) {

            String nameValue = (String) nameField.getValue();
            String passValue = (String) passField.getValue();

            if (nameValue != null && passValue != null) {
                if (appBody.login(nameValue, passValue)) {
                    appHeader.setLoggedIn(nameValue);
                    DifferApplication.getCurrentApplication().getMainWindow().showNotification("Success", "<br/>You are now logged in as " + nameValue);
                    GUIMacros.closeWindow(LoginUserWindow.this);
                }
            }
        }
    }

    public LoginUserWindow(DifferProgramTab appBody, ProjectHeaderPanel appHeader) {
        super("Login");
        this.appBody = appBody;
        this.appHeader = appHeader;

        addComponent(createLoginUserWindow());

        HorizontalLayout buttonLayout = new HorizontalLayout();

        Button login = new Button("Login");
        login.setClickShortcut(KeyCode.ENTER);
        login.addListener(new LoginButtonClickListener());
        buttonLayout.addComponent(login);

        Button close = new Button("Close");
        close.addListener(GUIMacros.createWindowCloseButtonListener(this));
        buttonLayout.addComponent(close);

        addComponent(buttonLayout);
    }

    private Layout createLoginUserWindow() {

        VerticalLayout layout = new VerticalLayout();

        nameField = new TextField("Username");
        nameField.addValidator(new NullValidator("You must provide a username!", false));
        layout.addComponent(nameField);

        passField = new PasswordField("Password");
        passField.addValidator(new NullValidator("You must provide a password!", false));
        layout.addComponent(passField);

        return layout;
    }
}
