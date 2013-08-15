package cz.nkp.differ.compare.io.generators;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.compare.io.CompareComponent;
import cz.nkp.differ.compare.io.ImageProcessorResult;
import cz.nkp.differ.compare.metadata.ImageMetadata;
import cz.nkp.differ.compare.metadata.MetadataSource;
import cz.nkp.differ.exceptions.FatalDifferException;
import cz.nkp.differ.gui.windows.RawDataWindow;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Thomas Truax
 */
public class ImageMetadataComponentGenerator {

    private ImageProcessorResult[] result;
    private List<String> nonConflictMetadata = Arrays.asList("exit-code");
    private CompareComponent parent;

    private static String TABLE_NAME         = "Metadata";
    private static String COLUMN_1_PROPERTY  = "key";
    private static String COLUMN_2_PROPERTY  = "source";
    private static String COLUMN_3_PROPERTY  = "image A value";
    private static String COLUMN_4_PROPERTY  = "image B value";
    private static String COLUMN_00_PROPERTY = "value"; //for comparison table only, not image table
    private static String COLUMN_5_PROPERTY  = "unit";
    
    /**
     * Constructor which takes a single ImageProcessorResult object (usually for comparison table)
     * @param ImageProcessorResult
     * @param CompareComponent 
     */
    public ImageMetadataComponentGenerator(ImageProcessorResult result, CompareComponent parent) {
        this.result = new ImageProcessorResult[] {result};
        this.parent = parent;
    }
    
    /**
     * Constructor which takes an array of ImageProcessorResult objects for multiple images
     * @param ImageProcessorResult[]
     * @param CompareComponent
     */
    public ImageMetadataComponentGenerator(ImageProcessorResult[] result, CompareComponent parent) {
        this.result = result;
        this.parent = parent;
    }
    
    /**
     * Retrieves table component after it has been generated
     * @return Layout
     */
    public Layout getComponent() {
        VerticalLayout layout = new VerticalLayout();
        layout.addStyleName("v-table-metadata");
        layout.setSpacing(true);
        try {
            generateMetadataTable(layout);
        } catch (FatalDifferException fde) {
            fde.printStackTrace();
        }
        return layout;
    }
    
    /**
     * Builds the table 
     * @param Layout 
     */
    private void generateMetadataTable(final Layout layout) throws FatalDifferException {
        final Table metadataTable;
                
        if (result.length == 2) {

            //Cannot use a BeanItemContainer here, since table no longer structured to match the ImageMetadata object

            //BeanItemContainer metadataContainer = new BeanItemContainer<ImageMetadata>(ImageMetadata.class, result.getMetadata());
            //metadataContainer.sort(new String[]{"key"}, new boolean[]{true});
            //final Table metadataTable = new Table("Metadata", metadataContainer);

            metadataTable = new Table(TABLE_NAME);
            metadataTable.addContainerProperty(COLUMN_1_PROPERTY, String.class, null);
            metadataTable.addContainerProperty(COLUMN_2_PROPERTY, Button.class, null);
            metadataTable.addContainerProperty(COLUMN_3_PROPERTY, Object.class, null);
            metadataTable.addContainerProperty(COLUMN_4_PROPERTY, Object.class, null);
            metadataTable.addContainerProperty(COLUMN_5_PROPERTY, String.class, null);
            
            List<ImageMetadata> imgAMetadataList = result[0].getMetadata();
            List<ImageMetadata> imgBMetadataList = result[1].getMetadata();
            
            if (imgAMetadataList.size() != imgBMetadataList.size()) {
                throw new FatalDifferException("Size of metadata lists for images A and B are not equal!");
            }

            for (int i = 0; i < result[0].getMetadata().size(); i++) {
                //Clickable tool names created here as button objects.
                //If need to make a specific item appear as regular text instead
                //use addStyleName + CSS to alter formatting

                ImageMetadata imgAMetadata = imgAMetadataList.get(i);
                ImageMetadata imgBMetadata = imgBMetadataList.get(i);

                if (imgAMetadata.getKey().contentEquals(imgBMetadata.getKey())) {

                    String key = imgAMetadata.getKey();
                    Button source = createClickableTool(layout, imgAMetadata.getSource());
                    Object valueA = imgAMetadata.getValue();
                    Object valueB = imgBMetadata.getValue();
                    String unit = imgAMetadata.getUnit();

                    metadataTable.addItem(new Object[] {key, source, valueA, valueB, unit}, i);
                }
            }
            metadataTable.sort(new String[] {COLUMN_1_PROPERTY}, new boolean[] {true});
            
        } else {
            //if only one single ImageProcessorResult object passed, create table normally with BeanItemContainer
            BeanItemContainer metadataContainer = new BeanItemContainer<ImageMetadata>(ImageMetadata.class, result[0].getMetadata());
            metadataContainer.sort(new String[]{COLUMN_1_PROPERTY}, new boolean[]{true});
            metadataTable = new Table(TABLE_NAME, metadataContainer);
            metadataTable.setVisibleColumns(new Object[]{COLUMN_1_PROPERTY, 
                                                         COLUMN_2_PROPERTY, 
                                                         COLUMN_00_PROPERTY, 
                                                         COLUMN_5_PROPERTY});
        }
        
        metadataTable.setSelectable(true);
        metadataTable.setMultiSelect(false);
        metadataTable.setImmediate(true);
            
        metadataTable.setCellStyleGenerator(new Table.CellStyleGenerator() {
            @Override
            public String getStyle(Object itemId, Object propertyId) {
                final ImageMetadata metadata;
                if (itemId instanceof Integer) {
                    metadata = result[0].getMetadata().get((Integer) itemId);
                } else {
                    metadata = (ImageMetadata) itemId;
                }
                if (result[0].getType() == ImageProcessorResult.Type.COMPARISON) {
                    String key = metadata.getKey();
                    if (Arrays.asList("red", "blue", "green").contains(key)) {
                        return key;
                    }
                } else {
                    if (!nonConflictMetadata.contains(metadata.getKey())) {
                        if (metadata.isConflict()) {
                            return "red";
                        } else {
                            return "green";
                        }
                    }
                }
                return "";
            }
        });
        layout.addComponent(metadataTable);

        final Button rawData = new Button();
        rawData.setCaption("Raw data");
        rawData.addListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    final ImageMetadata metadata;
                    if (metadataTable.getValue() instanceof Integer) {
                        metadata = result[0].getMetadata().get((Integer) metadataTable.getValue());
                    } else {
                        metadata = (ImageMetadata) metadataTable.getValue();
                    }
                    Window rawDataWindow = new RawDataWindow(parent, metadata.getSource());
                    Window mainWindow = DifferApplication.getMainApplicationWindow();
                    mainWindow.addWindow(rawDataWindow);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        rawData.setImmediate(true);
        rawData.setEnabled(true); 
        layout.addComponent(rawData);
        metadataTable.addListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                rawData.setEnabled(true);
            }
        });
    }
    
    private Button createClickableTool(final Layout layout, MetadataSource source) {
       
        String toolName = "unknown";
        if (source.getSourceName() != null && source.getSourceName().length() > 0) {
            toolName = source.getSourceName();
        }
        Button button = new Button(toolName);
        final String version;
        if (source.getVersion() != null && source.getVersion().length() > 0) {
            version = source.getVersion();
        } else {
            version = "Tool version unknown";
        }
        button.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                layout.getWindow().showNotification(version);
            } 
        });
        button.addStyleName("link");
        
        return button;
    }
}
