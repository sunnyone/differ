package cz.nkp.differ.compare.io.generators;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.terminal.Sizeable;
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
import cz.nkp.differ.compare.io.GlitchDetectorConfig;
import cz.nkp.differ.compare.io.GlitchDetectorResultPostProcessor;
import cz.nkp.differ.compare.io.ImageProcessorResult;
import cz.nkp.differ.compare.metadata.ImageMetadata;
import cz.nkp.differ.compare.metadata.JP2ProfileValidationResult;
import cz.nkp.differ.compare.metadata.MetadataGroups;
import cz.nkp.differ.compare.metadata.ValidatedProperty;
import cz.nkp.differ.exceptions.FatalDifferException;
import cz.nkp.differ.gui.windows.GlitchDetectorWindow;
import cz.nkp.differ.gui.windows.JP2ProfileValidationResultWindow;
import cz.nkp.differ.gui.windows.RawDataWindow;
import cz.nkp.differ.tools.GlossaryUtil;
import cz.nkp.differ.tools.HTMLGlossaryUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
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
    
    private static String VERSION_PROPERTY_NAME   = "Version";
    private static String COLUMN_KEY_PROPERTY     = "key";
    private static String COLUMN_SOURCE_PROPERTY  = "source";
    private static String COLUMN_A_VALUE_PROPERTY = "imageValueA";
    private static String COLUMN_B_VALUE_PROPERTY = "imageValueB";
    private static String COLUMN_UNIT_PROPERTY    = "unit";
    
    private static String COLUMN_VALUE_PROPERTY    = "value"; //not used in imageA\imageB table
    
    private static String COLUMN_TOOL_PROPERTY    = "tool";
    private static String COLUMN_VERSION_PROPERTY = "version";
    private static String COLUMN_A_TIME_PROPERTY  = "image A time";
    private static String COLUMN_B_TIME_PROPERTY  = "image B time";
    
    private GlossaryUtil glossaryUtil = new HTMLGlossaryUtil();
    
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
                if (!nonConflictMetadata.contains(metadata.getKey())) {
                    if (metadata.isConflict()) {
                        return "red";
                    } else {
                        return "green";
                    }
                }
                return "";
            }
        });
        layout.addComponent(metadataTable);
        return metadataTable;
    }

    private HashMap<String, ComparedImagesMetadata> getMetadataTable(List<ImageMetadata>[] datas) {
	HashMap<String, ComparedImagesMetadata> hashmap = new HashMap<String, ComparedImagesMetadata>();
        for (int resultIndex = 0; resultIndex < datas.length; resultIndex++) {
	    if (datas[resultIndex] == null) {
		return hashmap;
	    }
            for (ImageMetadata data : datas[resultIndex]) {
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
	final Map<String, ComparedImagesMetadata> glitchProps = filterBySource(GlitchDetectorResultPostProcessor.SOURCE_NAME, metadata);
	//generateMetadataTableForTwoResults(layout, "Used extractors", filterByProperties(metadata, metadataGroups.getExtractorProperties()));
        generateMetadataTableForUsedExtractors(layout, metadata);
	generateMetadataTableForTwoResults(layout, "Identification", filterByProperties(metadata, metadataGroups.getIdentificationProperties()));
	generateMetadataTableForTwoResults(layout, "Validation", filterByProperties(metadata, metadataGroups.getValidationProperties()));
	generateMetadataTableForTwoResults(layout, "Characterization", filterByProperties(metadata, metadataGroups.getCharacterizationProperties()));
	if (!profileProps.isEmpty()) {
	    generateMetadataTableForTwoResults(layout, "JPEG2000 profile validation", profileProps);
	}
	generateMetadataTableForTwoResults(layout, "Others", filterByOtherProperties(metadata, metadataGroups.getAllProperties()));

	final GlitchDetectorConfig config = new GlitchDetectorConfig();
	if (!glitchProps.isEmpty()) {
	    final Table table = generateMetadataTableForTwoResults(GlitchDetectorResultPostProcessor.SOURCE_NAME, glitchProps);
	    layout.addComponent(table);
	    final Button glitchSettingButton = new Button("settings");
	    glitchSettingButton.addListener(new ClickListener() {

		private Table newTable = null;

		@Override
		public void buttonClick(ClickEvent event) {
		    final GlitchDetectorWindow window = new GlitchDetectorWindow(config);
		    window.setOnSubmit(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
			    if (newTable == null) {
				layout.removeComponent(table);
			    } else {
				layout.removeComponent(newTable);
			    }
			    layout.removeComponent(glitchSettingButton);
			    GlitchDetectorResultPostProcessor processor = DifferApplication.getApplicationContext().getBean(GlitchDetectorResultPostProcessor.class);
			    List<ImageMetadata>[] datas = (List<ImageMetadata>[]) new List[result.length + 1];
			    int index = 0;
			    for (ImageProcessorResult res : result) {
				datas[index] = processor.process(res, config);
				index++;
			    }
			    HashMap<String, ComparedImagesMetadata> newGlitchProps = ImageMetadataComponentGenerator.this.getMetadataTable(datas);
			    newTable = generateMetadataTableForTwoResults(GlitchDetectorResultPostProcessor.SOURCE_NAME, newGlitchProps);
			    layout.addComponent(newTable);
			    layout.addComponent(glitchSettingButton);
			}
		    });
		    window.init();
		    parent.getMainWindow().addWindow(window);
		}
	    });
	    glitchSettingButton.setImmediate(true);
	    layout.addComponent(glitchSettingButton);
	}
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
        
        private final Object data;

        SortableButton(String label, Object data) {
            super(label);
            this.label = label;
            this.data = data;
        }

        @Override
        public int compareTo(SortableButton other) {
            return label.compareTo(other.label);
        }
        
        public Object getData() {
            return data;
        }

    }

    private Table generateMetadataTableForTwoResults(String group, Map<String, ComparedImagesMetadata> hashmap) {
	final Table metadataTable = new Table(group.toUpperCase());
        metadataTable.addContainerProperty(COLUMN_KEY_PROPERTY, SortableButton.class, null);
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
            Button key = createClickableKey(cim.getKey());
            Button clickableToolName = createClickableTool(cim.getSourceName(), cim.getVersion());

            Button valueA = createClickableValue(cim.getImageMetadata()[0]);
            Button valueB = createClickableValue(cim.getImageMetadata()[1]);
            metadataTable.addItem(new Object[] { key, clickableToolName, valueA,
                        valueB, cim.getUnit() }, row);
            row++;
        }

        metadataTable.setSelectable(true);
        metadataTable.setMultiSelect(false);
        metadataTable.setImmediate(true);
        metadataTable.setWidth(2 * TABLE_WIDTH, Sizeable.UNITS_PIXELS);
        metadataTable.setPageLength(Math.min(row, 10));
        metadataTable.sort(new Object[]{ COLUMN_KEY_PROPERTY }, new boolean[] {true });

        if (group.equals("JPEG2000 profile validation")) {
            metadataTable.setCellStyleGenerator(new ValidatedPropertyCellStyleGenerator(metadataTable));
        } else if (group.equals(GlitchDetectorResultPostProcessor.SOURCE_NAME)) {
            metadataTable.setCellStyleGenerator(new ValidatedPropertyCellStyleGenerator(metadataTable));
        } else {
            metadataTable.setCellStyleGenerator(new ConflictCellStyleGenerator(metadataTable));
        }	
	return metadataTable;
    }
    
    private void generateMetadataTableForUsedExtractors(final Layout layout, Map<String, ComparedImagesMetadata> hashmap) {
        final Table metadataTable = new Table("Used extractors".toUpperCase());
        metadataTable.addContainerProperty(COLUMN_TOOL_PROPERTY, String.class, null);
        metadataTable.addContainerProperty(COLUMN_VERSION_PROPERTY, String.class, null);
        metadataTable.addContainerProperty(COLUMN_A_TIME_PROPERTY, String.class, null);
        metadataTable.addContainerProperty(COLUMN_B_TIME_PROPERTY, String.class, null);
        int row = 0;
        for (ComparedImagesMetadata metadata : hashmap.values()) {
            if (metadata.getKey().equals("Elapsed Time of Extraction")) {
                metadataTable.addItem(new Object[] { metadata.getSourceName(), metadata.getVersion(),
                    metadata.getImageMetadata()[0].getValue().toString(), metadata.getImageMetadata()[1].getValue().toString() }, row);
                row++;
            }
        }
        metadataTable.setWidth(2 * TABLE_WIDTH, Sizeable.UNITS_PIXELS);
        metadataTable.setPageLength(row);
        metadataTable.setImmediate(true);
        layout.addComponent(metadataTable);
    }
    
    private void generateMetadataTableForTwoResults(final Layout layout, String group, Map<String, ComparedImagesMetadata> hashmap) {
	layout.addComponent(generateMetadataTableForTwoResults(group, hashmap));
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
    
    private class ValidatedPropertyCellStyleGenerator implements Table.CellStyleGenerator {
        
        private Table metadataTable;

        public ValidatedPropertyCellStyleGenerator(Table metadataTable) {
            this.metadataTable = metadataTable;
        }
        
        @Override
        public String getStyle(Object itemId, Object propertyId) {
            if (itemId != null && propertyId != null) {
                Object prop = (Object) metadataTable.getContainerProperty(itemId, propertyId).getValue();
                if (prop != null && prop instanceof SortableButton) {
                    SortableButton butt = (SortableButton) prop;
                    if (butt.getData() instanceof ValidatedProperty) {
                        ValidatedProperty property = (ValidatedProperty) butt.getData();
                        return (property.isValid())? "green" : "red";
                    }
                }
            }
            return "";
        }
    }
    
    private SortableButton createClickableTool(String source, String version) {       
        final String toolName = (source == null || source.isEmpty()) ? "tool name unknown" : source;
        final String ver = (version == null || version.isEmpty()) ? "unknown" : version;
        SortableButton button = new SortableButton(toolName, null);
        button.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                parent.getMainWindow().showNotification(toolName, "<br/>version " + ver, Window.Notification.TYPE_HUMANIZED_MESSAGE);
            } 
        });
        button.addStyleName("link");
        return button;
    }
    
    private SortableButton createClickableValue(final ImageMetadata data) {
	String value = "";
        Object dataValue = null;
        if (data != null && data.getValue() != null) {
            value = data.getValue().toString();
            dataValue = data.getValue();
        }
        SortableButton button = new SortableButton(value, dataValue);
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
                    parent.getMainWindow().addWindow(window);
                }
            });
        }
        button.addStyleName("link");
        return button;
    }
    
    private SortableButton createClickableKey(final String key) {
        SortableButton button = new SortableButton(key, key);
        final String glossary = this.glossaryUtil.getGlossaryFor(key, Locale.ENGLISH,"glossary");
        if (glossary != null) {
            button.addListener(new Button.ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
		    parent.getMainWindow().showNotification(key, glossary);
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
