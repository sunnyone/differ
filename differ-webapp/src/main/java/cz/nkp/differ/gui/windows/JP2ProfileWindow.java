package cz.nkp.differ.gui.windows;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.compare.metadata.JP2Profile;
import cz.nkp.differ.gui.components.JP2ProfileForm;
import cz.nkp.differ.profile.EditableJP2ProfileProvider;

/**
 *
 * @author xrosecky
 */
public class JP2ProfileWindow extends Window {
    
    private EditableJP2ProfileProvider jp2ProfileProvider;
    
    private Layout profileForm = null;
    
    private final VerticalLayout windowLayout;
    
    private ComboBox profileSelector;
    
    private static class ProfileItem {
        
        private JP2Profile profile;

        public ProfileItem(JP2Profile jp2Profile) {
            this.profile = jp2Profile;
        }
        
        @Override
        public String toString() {
            return profile.getName();
        }
        
        public JP2Profile getProfile() {
            return profile;
        }
        
    }
    
    public JP2ProfileWindow() {
        jp2ProfileProvider = DifferApplication.getEditableJP2ProfileProvider();
        setCaption("JPEG2000 profile editor");
	setModal(true);
	setDraggable(false);
	setResizable(false);
	center();
	setWidth("30%");
        windowLayout = new VerticalLayout();
        windowLayout.setSpacing(true);
        profileForm = createProfileCreationWindowForm(new JP2Profile());
        windowLayout.addComponent(createProfileSelectionWindowForm());
        windowLayout.addComponent(profileForm);
        addComponent(windowLayout);
    }
    
    private Layout createProfileCreationWindowForm(JP2Profile profile) {
        VerticalLayout layout = new VerticalLayout();
        final JP2ProfileForm form = new JP2ProfileForm(profile);
        layout.addComponent(form.getProfileForm());
        Button submit = new Button("Submit");
        submit.addListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                JP2Profile profile = form.getJp2Profile();
                Object obj = profileSelector.getValue();
                if (obj != null && obj instanceof ProfileItem) {
                    jp2ProfileProvider.update(profile);
                    JP2ProfileWindow.this.refresh();
                } else if (obj!= null && obj instanceof String) {
                    profile.setName((String) obj);
                    jp2ProfileProvider.saveNew(profile);
                    JP2ProfileWindow.this.refresh();
                }
            }
            
        });
        layout.addComponent(submit);
        return layout;
    }
    
    private Layout createProfileSelectionWindowForm() {
        profileSelector = new ComboBox("Profile");
        profileSelector.setInputPrompt("Name of your profile");
        for (JP2Profile profile : jp2ProfileProvider.getProfiles()) {
            profileSelector.addItem(new ProfileItem(profile));
        }
        profileSelector.setTextInputAllowed(true);
        profileSelector.setNewItemsAllowed(true);
        Button editoOrCreateProfileButton = new Button("Edit / Create");
        editoOrCreateProfileButton.addListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                windowLayout.removeComponent(profileForm);
                Object obj = profileSelector.getValue();
                if (obj != null && obj instanceof ProfileItem) {
                    ProfileItem profile = (ProfileItem) obj;
                    profileForm = createProfileCreationWindowForm(profile.getProfile());
                    windowLayout.addComponent(profileForm);
                } else {
                    JP2Profile profile = new JP2Profile();
                    profileForm = createProfileCreationWindowForm(profile);
                    windowLayout.addComponent(profileForm);
                }
            }
        });
        
        HorizontalLayout profilePanel = new HorizontalLayout();
        profilePanel.addComponent(profileSelector);
        profilePanel.addComponent(editoOrCreateProfileButton);
        return profilePanel;
    }
    
    private void refresh() {
        Object selected = profileSelector.getValue();
        String selectedName = null;
        if (selected instanceof String) {
            selectedName = (String) selected;
        } else if (selected instanceof ProfileItem) {
            selectedName = ((ProfileItem) selected).getProfile().getName();
        }
        profileSelector.removeAllItems();
        for (JP2Profile profile : jp2ProfileProvider.getProfiles()) {
            ProfileItem item = new ProfileItem(profile);
            profileSelector.addItem(item);
            if (profile.getName().equals(selectedName)) {
                profileSelector.setValue(item);
            }
        }
    }
    
}
