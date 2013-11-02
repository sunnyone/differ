package cz.nkp.differ.compare.io.generators;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.compare.io.CompareComponent;
import cz.nkp.differ.compare.io.ComparedImagesMetadata;
import cz.nkp.differ.compare.io.ImageProcessorResult;
import cz.nkp.differ.compare.metadata.ImageMetadata;
import cz.nkp.differ.compare.metadata.MetadataSource;
import cz.nkp.differ.exceptions.FatalDifferException;
import cz.nkp.differ.gui.windows.RawDataWindow;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Thomas Truax
 */
public class ImageMetadataComponentGenerator {

    private ImageProcessorResult[] result;
    private List<String> nonConflictMetadata = Arrays.asList("exit-code");
    private CompareComponent parent;

    private String tableName = "METADATA";
    
    private static String VERSION_PROPERTY_NAME = "Version";
    private static String COLUMN_A1_PROPERTY    = "key";
    private static String COLUMN_A2_PROPERTY    = "source";
    private static String COLUMN_A3_PROPERTY    = "imageValueA";
    private static String COLUMN_A4_PROPERTY    = "imageValueB";
    private static String COLUMN_A5_PROPERTY    = "unit";
    
    private static String COLUMN_B1_PROPERTY    = "value"; //not used in imageA\imageB table
    
    private static String COLUMN_C1_PROPERTY    = "metadataSource";
    private static String COLUMN_C2_PROPERTY    = "version";
    
    /**
     * Constructor which takes a single ImageProcessorResult object (usually for comparison table)
     * @param ImageProcessorResult
     * @param CompareComponent 
     */
    public ImageMetadataComponentGenerator(ImageProcessorResult result, CompareComponent parent) {
        this(new ImageProcessorResult[] {result}, parent);
    }
    
    /**
     * Constructor which takes an array of ImageProcessorResult objects for multiple images
     * @param ImageProcessorResult[]
     * @param CompareComponent
     */
    public ImageMetadataComponentGenerator(ImageProcessorResult[] result, CompareComponent parent) {
        //FIXME: if results.length > 2, throw unsupported number of args exception
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
        //TODO: add logic for handling array length > 2
        if (result.length == 2) {
            generateMetadataTableForTwoResults(layout);
        } else if (result.length == 1) {
            generateMetadataTableForSingleResult(layout);
        } else {
            throw new IllegalArgumentException("More than two results is not supported");
        }
    }
    
    //if only one single ImageProcessorResult object passed, create table with BeanItemContainer
    private Table generateMetadataTableForSingleResult(final Layout layout) {
        BeanItemContainer metadataContainer =
                new BeanItemContainer<ImageMetadata>(ImageMetadata.class, result[0].getMetadata());
        final Table metadataTable = new Table(tableName, metadataContainer);
        metadataTable.setVisibleColumns(new Object[]{COLUMN_A1_PROPERTY, COLUMN_A2_PROPERTY,
                    COLUMN_B1_PROPERTY, COLUMN_A5_PROPERTY});
        metadataTable.setColumnWidth(COLUMN_B1_PROPERTY, 200);
        metadataTable.sort(new String[]{COLUMN_A1_PROPERTY}, new boolean[]{true});
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
        return metadataTable;
    }
    
    private Table generateMetadataTableForTwoResults(final Layout layout) {
        final Table metadataTable;

        HashMap<String, ComparedImagesMetadata> hashmap = new HashMap<String, ComparedImagesMetadata>();

        for (int resultIndex = 0; resultIndex < result.length; resultIndex++) {
            for (ImageMetadata data : result[resultIndex].getMetadata()) {
                String id = data.getKey() + "&&" + data.getSource().getSourceName();
                ComparedImagesMetadata cim = hashmap.get(id);
                if (cim == null) {
                    cim = new ComparedImagesMetadata(id);
                    cim.setKey(data.getKey());
                    cim.setUnit(data.getUnit());
                    cim.setConflict(data.isConflict());
                    MetadataSource msrc = data.getSource();
                    cim.setMetadataSource(msrc);
                    cim.setSourceName(data.getSource().getSourceName());
                    String[] values = new String[result.length + 1];
                    if (data.getValue() != null) {
                        values[resultIndex] = data.getValue().toString();
                    }
                    cim.setValues(values);
                    hashmap.put(id, cim);
                } else {
                    if (data.getValue() != null) {
                        String[] values = cim.getValues();
                        values[resultIndex] = data.getValue().toString();
                    }
                }
            }
        }

        metadataTable = new Table(tableName);
        metadataTable.addContainerProperty(COLUMN_A1_PROPERTY, String.class, null);
        metadataTable.addContainerProperty(COLUMN_A2_PROPERTY, Button.class, null);
        metadataTable.addContainerProperty(COLUMN_A3_PROPERTY, String.class, null);
        metadataTable.addContainerProperty(COLUMN_A4_PROPERTY, String.class, null);
        metadataTable.addContainerProperty(COLUMN_A5_PROPERTY, String.class, null);
        metadataTable.addContainerProperty(COLUMN_C2_PROPERTY, String.class, null);
        metadataTable.addContainerProperty(COLUMN_C1_PROPERTY, MetadataSource.class, null);

        //prevent column overflow
        metadataTable.setColumnWidth(COLUMN_A3_PROPERTY, 200);
        metadataTable.setColumnWidth(COLUMN_A4_PROPERTY, 200);
        metadataTable.setColumnWidth(COLUMN_C2_PROPERTY, 100);

        //first iteration merely cleans the version property from the various tools
        HashMap<String, String> versionmap = new HashMap<String, String>();
        for (Map.Entry<String, ComparedImagesMetadata> entry : hashmap.entrySet()) {
            if (entry.getKey().equals(VERSION_PROPERTY_NAME)) {
                ComparedImagesMetadata cim = entry.getValue();
                versionmap.put(cim.getSourceName(), cim.getValues()[0]);
            }
        }

        int row = 0;
        for (Map.Entry<String, ComparedImagesMetadata> entry : hashmap.entrySet()) {
            ComparedImagesMetadata cim = entry.getValue();
            cim.setVersion(versionmap.get(cim.getSourceName()));
            MetadataSource msrc = cim.getMetadataSource();
            cim.setSource(createClickableTool(layout, msrc.getSourceName(), cim.getVersion()));
            metadataTable.addItem(new Object[]{cim.getKey(), cim.getSource(), cim.getValues()[0],
                        cim.getValues()[1], cim.getUnit(),
                        cim.getVersion(), cim.getMetadataSource()}, row);
            row++;
        }

        //the following line must be set AFTER adding ALL data to the table
        //otherwise data will not be set properly (table will be empty)
        metadataTable.setVisibleColumns(new Object[]{ COLUMN_A1_PROPERTY, COLUMN_A2_PROPERTY, COLUMN_A3_PROPERTY,
                    COLUMN_A4_PROPERTY, COLUMN_A5_PROPERTY });
        
        metadataTable.sort(new String[]{COLUMN_A1_PROPERTY}, new boolean[]{true});
        metadataTable.setSelectable(true);
        metadataTable.setMultiSelect(false);
        metadataTable.setImmediate(true);
        layout.addComponent(metadataTable);
        
        metadataTable.setCellStyleGenerator(new Table.CellStyleGenerator() {
            @Override
            public String getStyle(Object itemId, Object propertyId) {

                if (result.length == 2) {
                    String valA = (String) metadataTable.getContainerProperty(itemId, COLUMN_A3_PROPERTY).getValue();
                    String valB = (String) metadataTable.getContainerProperty(itemId, COLUMN_A4_PROPERTY).getValue();
                    if (valA != null && valB != null) {
                        if (valA.equalsIgnoreCase(valB)) {
                            return "green";
                        }
                    }
                    return "red";
                }
                return "";
            }
        });
        
        final Button rawData = new Button("Raw data");
        rawData.addListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                Item selectedRow = metadataTable.getItem((Integer) metadataTable.getValue());
                Property rowData = selectedRow.getItemProperty("metadataSource");
                MetadataSource metadata = (MetadataSource) rowData.getValue();
                Window rawDataWindow = new RawDataWindow(parent, metadata);
                Window mainWindow = DifferApplication.getMainApplicationWindow();
                mainWindow.addWindow(rawDataWindow);
            }
        });
        metadataTable.addListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                rawData.setEnabled(true);
            }
        });
        rawData.setImmediate(true);
        rawData.setEnabled(true);
        layout.addComponent(rawData);
        
        return metadataTable;
    }
    
    private Button createClickableTool(final Layout layout, String source, String version) {       
        final String toolName = (source == null || source.isEmpty()) ? "tool name unknown" : source;
        final String ver = (version == null || version.isEmpty()) ? "unknown" : version;
        Button button = new Button(toolName);
        button.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                layout.getWindow().showNotification(toolName, "<br/>version " + ver, Window.Notification.TYPE_HUMANIZED_MESSAGE);
            } 
        });
        button.addStyleName("link");
        return button;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
