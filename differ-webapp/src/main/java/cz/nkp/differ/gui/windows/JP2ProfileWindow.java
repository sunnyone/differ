package cz.nkp.differ.gui.windows;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Form;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import cz.nkp.differ.compare.metadata.JP2Kernel;
import cz.nkp.differ.compare.metadata.JP2Profile;
import cz.nkp.differ.compare.metadata.JP2Size;
import cz.nkp.differ.compare.metadata.external.JP2ProfileProvider;
import cz.nkp.differ.gui.components.JP2ProfileForm;
import java.util.Arrays;

/**
 *
 * @author xrosecky
 */
public class JP2ProfileWindow extends Window {
    
    private JP2ProfileProvider jp2ProfileProvider;
    
    public JP2ProfileWindow() {
        setCaption("JPEG2000 profile editor");
	setModal(true);
	setDraggable(false);
	setResizable(false);
	center();
	setWidth("25%");
        VerticalLayout windowLayout = new VerticalLayout();
        windowLayout.setSpacing(true);
        windowLayout.addComponent(createProfileCreationWindowForm());
        addComponent(windowLayout);
    }
    
    private Layout createProfileCreationWindowForm() {
        VerticalLayout layout = new VerticalLayout();
        JP2Profile profile = new JP2Profile();
        profile.setKernel(JP2Kernel.Revesible5x3);
        profile.setMaxQualityLayers(4);
        profile.setMinQualityLayers(1);
        profile.setTileSizes(Arrays.asList(new JP2Size(1024, 1024), new JP2Size(4096, 4096)));
        profile.setPreccintSizes(Arrays.asList(new JP2Size(64, 64)));
        final JP2ProfileForm profileForm = new JP2ProfileForm(profile);
        layout.addComponent(profileForm.getProfileForm());
        Button submit = new Button();
        submit.addListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                profileForm.getJp2Profile();
            }
            
        });
        layout.addComponent(submit);
        return layout;
    }
    
}
