package cz.nkp.differ.gui.windows;

import com.vaadin.data.validator.NullValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.gui.components.CaptchaComponent;
import cz.nkp.differ.model.User;
import cz.nkp.differ.util.GUIMacros;

public class RegisterUserWindow extends CustomWindow {

    private TextField nameField;
    private PasswordField passField;
    private CaptchaComponent captcha = null;

    private class RegisterButtonClickListener implements ClickListener {

        @Override
        public void buttonClick(ClickEvent event) {
            try {
                processButtonClick(event);
            } catch (Exception ex) {
                DifferApplication.getCurrentApplication().getMainWindow().showNotification("Error when registering user", "<br/>" + ex.getMessage(), Window.Notification.TYPE_ERROR_MESSAGE);
                if (captcha != null) {
                    captcha.reset();
                }
            }
        }

        private void processButtonClick(ClickEvent event) throws Exception {
            if (captcha != null && !captcha.passedValidation()) {
                throw new Exception("You did not enter the correct captcha, please try again");
            }

            String nameValue = (String) nameField.getValue();
            String passValue = (String) passField.getValue();
            if (nameValue.isEmpty() || passValue.isEmpty()) {
                throw new Exception("The Username and Password fields cannot be empty");
            }

            //FIXME: ensure no special characters are used in username
            if (DifferApplication.getUserManager().findByUserName(nameValue) == null) { // if username doesnt exist
                User user = new User();
                user.setUserName(nameValue);
                DifferApplication.getUserManager().registerUser(user, passValue);
                DifferApplication.getCurrentApplication().getMainWindow().showNotification("Success",
                        "<br/>You have successfully registered as " + nameField + ", you may now login");
                GUIMacros.closeWindow(RegisterUserWindow.this);
            } else { //else username already exists
                throw new Exception("The username you have chosen already exists");
            }
        }
    }

    public RegisterUserWindow() {
        super("Register User");

        addComponent(createRegisterUserWindow());

        HorizontalLayout buttonLayout = new HorizontalLayout();

        Button register = new Button("Register");
        register.addListener(new RegisterButtonClickListener());
        buttonLayout.addComponent(register);

        Button close = new Button("Close");
        close.addListener(GUIMacros.createWindowCloseButtonListener(this));
        buttonLayout.addComponent(close);

        addComponent(buttonLayout);
    }

    private Layout createRegisterUserWindow() {

        VerticalLayout layout = new VerticalLayout();

        nameField = new TextField("Username");
        nameField.addValidator(new NullValidator("You must provide a username!", false));
        layout.addComponent(nameField);

        passField = new PasswordField("Password");
        passField.addValidator(new NullValidator("You must provide a password!", false));
        layout.addComponent(passField);

        if (DifferApplication.getConfiguration().isCaptchaEnabled()) {
            captcha = new CaptchaComponent();
            layout.addComponent(captcha);
        }

        layout.setSpacing(true);

        return layout;
    }
}
