package cz.nkp.differ.compare.io.generators;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.compare.io.CompareComponent;
import cz.nkp.differ.compare.io.ComparedImagesMetadata;
import cz.nkp.differ.compare.io.GlitchDetectorResultPostProcessor;
import cz.nkp.differ.compare.io.ImageProcessorResult;
import cz.nkp.differ.compare.metadata.ImageMetadata;
import cz.nkp.differ.compare.metadata.JP2ProfileValidationResult;
import cz.nkp.differ.compare.metadata.MetadataGroups;
import cz.nkp.differ.exceptions.FatalDifferException;
import cz.nkp.differ.gui.windows.JP2ProfileValidationResultWindow;
import cz.nkp.differ.gui.windows.RawDataWindow;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Thomas Truax
 */
public class ImageMetadataComponentGenerator {

    private ImageProcessorResult[] result;
    private List<String> nonConflictMetadata = Arrays.asList("exit-code");
    private CompareComponent parent;

    private String tableName = "METADATA";
    
    private static int TABLE_WIDTH = 400;
    
    private static String VERSION_PROPERTY_NAME   =  "Version";
    private static String COLUMN_KEY_PROPERTY     = "key";
    private static String COLUMN_SOURCE_PROPERTY  = "source";
    private static String COLUMN_A_VALUE_PROPERTY = "imageValueA";
    private static String COLUMN_B_VALUE_PROPERTY = "imageValueB";
    private static String COLUMN_UNIT_PROPERTY    = "unit";
    
    private static String COLUMN_VALUE_PROPERTY    = "value"; //not used in imageA\imageB table
    
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
        metadataTable.setVisibleColumns(new Object[]{COLUMN_KEY_PROPERTY, COLUMN_SOURCE_PROPERTY,
                    COLUMN_VALUE_PROPERTY, COLUMN_UNIT_PROPERTY});
        metadataTable.setColumnWidth(COLUMN_VALUE_PROPERTY, 200);
        metadataTable.sort(new String[]{COLUMN_KEY_PROPERTY}, new boolean[]{true});
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
    
    private HashMap<String, ComparedImagesMetadata> getMetadataTable() {
        HashMap<String, ComparedImagesMetadata> hashmap = new HashMap<String, ComparedImagesMetadata>();
        for (int resultIndex = 0; resultIndex < result.length; resultIndex++) {
            for (ImageMetadata data : result[resultIndex].getMetadata()) {
		String sourceName = "unknown";
		if (data.getSource() != null && data.getSource().getSourceName() != null) {
		    sourceName = data.getSource().getSourceName();
		}
                String id = data.getKey() + "&&" + sourceName;
                ComparedImagesMetadata cim = hashmap.get(id);
                if (cim == null) {
                    cim = new ComparedImagesMetadata(id);
                    cim.setKey(data.getKey());
                    cim.setUnit(data.getUnit());
                    cim.setConflict(data.isConflict());
                    cim.setSourceName(sourceName);
                    ImageMetadata[] metadata = new ImageMetadata[result.length + 1];
                    if (data.getValue() != null) {
                        metadata[resultIndex] = data;
                    }
                    cim.setImageMetadata(metadata);
                    hashmap.put(id, cim);
                } else {
                    if (data.getValue() != null) {
                        ImageMetadata[] metadata = cim.getImageMetadata();
                        metadata[resultIndex] = data;
                    }
                }
            }
        }
        return hashmap;
    }
    
    private void generateMetadataTableForTwoResults(final Layout layout) {
	HashMap<String, ComparedImagesMetadata> metadata = getMetadataTable();
	MetadataGroups metadataGroups = DifferApplication.getMetadataGroups();
	Map<String, ComparedImagesMetadata> profileProps = filterBySource("Profile validation", metadata);
	Map<String, ComparedImagesMetadata> glitchProps = filterBySource(GlitchDetectorResultPostProcessor.SOURCE_NAME, metadata);
	generateMetadataTableForTwoResults(layout, "Used extractors", filterByProperties(metadata, metadataGroups.getExtractorProperties()));
	generateMetadataTableForTwoResults(layout, "Identification", filterByProperties(metadata, metadataGroups.getIdentificationProperties()));
	generateMetadataTableForTwoResults(layout, "Validation", filterByProperties(metadata, metadataGroups.getValidationProperties()));
	generateMetadataTableForTwoResults(layout, "Characterization", filterByProperties(metadata, metadataGroups.getCharacterizationProperties()));
	if (!profileProps.isEmpty()) {
	    generateMetadataTableForTwoResults(layout, "JPEG2000 profile validation", profileProps);
	}
	if (!glitchProps.isEmpty()) {
	    generateMetadataTableForTwoResults(layout, GlitchDetectorResultPostProcessor.SOURCE_NAME, glitchProps);
	}
	generateMetadataTableForTwoResults(layout, "Others", filterByOtherProperties(metadata, metadataGroups.getAllProperties()));
    }
    
    private Map<String, ComparedImagesMetadata> filterBySource(String sourceName, HashMap<String, ComparedImagesMetadata> metadata) {
	Set<String> propertiesToRemove = new HashSet<String>();
	Map<String, ComparedImagesMetadata> results = new HashMap<String, ComparedImagesMetadata>();
	for (Map.Entry<String, ComparedImagesMetadata> entry : metadata.entrySet()) {
	    if (sourceName.equals(entry.getValue().getSourceName())) {
		results.put(entry.getKey(), entry.getValue());
		propertiesToRemove.add(entry.getKey());
	    }
	}
	for (String propertyToRemove : propertiesToRemove) {
	    metadata.remove(propertyToRemove);
	}
	return results;
    }
    
    private Map<String, ComparedImagesMetadata> filterByProperties(HashMap<String, ComparedImagesMetadata> metadata, Set<String> allowed) {
        Map<String, ComparedImagesMetadata> results = new HashMap<String, ComparedImagesMetadata>();
        for (Map.Entry<String, ComparedImagesMetadata> entry : metadata.entrySet()) {
            if (allowed.contains(entry.getValue().getKey())) {
                results.put(entry.getKey(), entry.getValue());
            }
        }
        return results;
    }
    
    private Map<String, ComparedImagesMetadata> filterByOtherProperties(HashMap<String, ComparedImagesMetadata> metadata, Set<String> allowed) {
        Map<String, ComparedImagesMetadata> results = new HashMap<String, ComparedImagesMetadata>();
        for (Map.Entry<String, ComparedImagesMetadata> entry : metadata.entrySet()) {
            if (!allowed.contains(entry.getValue().getKey())) {
                results.put(entry.getKey(), entry.getValue());
            }
        }
        return results;
    }
    
    private static class SortableButton extends Button implements Comparable<SortableButton> {

        private final String label;

        SortableButton(String label) {
            super(label);
            this.label = label;
        }

        @Override
        public int compareTo(SortableButton other) {
            return label.compareTo(other.label);
        }

    }
    
    private void generateMetadataTableForTwoResults(final Layout layout, String group, Map<String, ComparedImagesMetadata> hashmap) {
        final Table metadataTable = new Table(group.toUpperCase());
        metadataTable.addContainerProperty(COLUMN_KEY_PROPERTY, String.class, null);
        metadataTable.addContainerProperty(COLUMN_SOURCE_PROPERTY, SortableButton.class, null);
        metadataTable.addContainerProperty(COLUMN_A_VALUE_PROPERTY, SortableButton.class, null);
        metadataTable.addContainerProperty(COLUMN_B_VALUE_PROPERTY, SortableButton.class, null);
        metadataTable.addContainerProperty(COLUMN_UNIT_PROPERTY, String.class, null);

        //prevent column overflow
        metadataTable.setColumnWidth(COLUMN_A_VALUE_PROPERTY, 200);
        metadataTable.setColumnWidth(COLUMN_B_VALUE_PROPERTY, 200);

        //first iteration merely cleans the version property from the various tools
        HashMap<String, String> versionmap = new HashMap<String, String>();
        for (Map.Entry<String, ComparedImagesMetadata> entry : hashmap.entrySet()) {
            if (entry.getKey().equals(VERSION_PROPERTY_NAME)) {
                ComparedImagesMetadata cim = entry.getValue();
                versionmap.put(cim.getSourceName(), cim.getImageMetadata()[0].getValue().toString());
            }
        }

        int row = 0;
        for (Map.Entry<String, ComparedImagesMetadata> entry : hashmap.entrySet()) {
            ComparedImagesMetadata cim = entry.getValue();
            Button clickableToolName = createClickableTool(layout, cim.getSourceName(), cim.getVersion());

            Button valueA = createClickableValue(cim.getImageMetadata()[0]);
            Button valueB = createClickableValue(cim.getImageMetadata()[1]);
            metadataTable.addItem(new Object[] { cim.getKey(), clickableToolName, valueA,
                        valueB, cim.getUnit() }, row);
            row++;
        }
        
        metadataTable.setSelectable(true);
        metadataTable.setMultiSelect(false);
        metadataTable.setImmediate(true);
        metadataTable.setWidth(2 * TABLE_WIDTH, Sizeable.UNITS_PIXELS);
        metadataTable.setPageLength(Math.min(row, 10));
        metadataTable.sort(new Object[]{ COLUMN_KEY_PROPERTY }, new boolean[] {true });
        layout.addComponent(metadataTable);
        
        if (group.equals("JPEG2000 profile validation")) {
            metadataTable.setCellStyleGenerator(new BooleanCellStyleGenerator(metadataTable));
        } else {
            metadataTable.setCellStyleGenerator(new ConflictCellStyleGenerator(metadataTable));
        }
    }
    
    private class ConflictCellStyleGenerator implements Table.CellStyleGenerator {
        
        private Table metadataTable;

        public ConflictCellStyleGenerator(Table metadataTable) {
            this.metadataTable = metadataTable;
        }
        
        @Override
        public String getStyle(Object itemId, Object propertyId) {
            if (result.length == 2 && propertyId == null) {
                Button valA = (Button) metadataTable.getContainerProperty(itemId, COLUMN_A_VALUE_PROPERTY).getValue();
                Button valB = (Button) metadataTable.getContainerProperty(itemId, COLUMN_B_VALUE_PROPERTY).getValue();
                if (valA != null && valB != null) {
                    String a = (String) valA.getCaption();
                    String b = (String) valB.getCaption();
                    if (a != null && b != null && !a.isEmpty() && !b.isEmpty()) {
                        return (a.equalsIgnoreCase(b)) ? "green" : "red";
                    }
                }
            }
            return "";
        }
        
    }
    
    private class BooleanCellStyleGenerator implements Table.CellStyleGenerator {
        
        private Table metadataTable;

        public BooleanCellStyleGenerator(Table metadataTable) {
            this.metadataTable = metadataTable;
        }
        
        @Override
        public String getStyle(Object itemId, Object propertyId) {
            if (itemId != null && propertyId != null) {
                Object a = (Object) metadataTable.getContainerProperty(itemId, propertyId).getValue();
                if (a != null && propertyId.toString().startsWith("imageValue")) {
                    String value = a.toString();
                    if (value.equals("true")) {
                        return "green";
                    } else if (value.equals("false")) {
                        return "red";
                    }
                }
            }
            return "";
        }
    }
    
    private SortableButton createClickableTool(final Layout layout, String source, String version) {       
        final String toolName = (source == null || source.isEmpty()) ? "tool name unknown" : source;
        final String ver = (version == null || version.isEmpty()) ? "unknown" : version;
        SortableButton button = new SortableButton(toolName);
        button.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                layout.getWindow().showNotification(toolName, "<br/>version " + ver, Window.Notification.TYPE_HUMANIZED_MESSAGE);
            } 
        });
        button.addStyleName("link");
        return button;
    }
    
    private SortableButton createClickableValue(final ImageMetadata data) {
	String value = "";
        if (data != null && data.getValue() != null) {
            value = data.getValue().toString();
        }
        SortableButton button = new SortableButton(value);
        if (data != null && data.getSource() != null) {
            button.addListener(new Button.ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
		    Window window = null;
		    if (data.getData() == null) {
			window = new RawDataWindow(parent, data.getSource());
		    } if (data.getData() instanceof JP2ProfileValidationResult) {
			window = new JP2ProfileValidationResultWindow((JP2ProfileValidationResult) data.getData());
		    }
                    Window mainWindow = DifferApplication.getMainApplicationWindow();
                    mainWindow.addWindow(window);
                }
            });
        }
        button.addStyleName("link");
        return button;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
