package cz.nkp.differ.compare.io.generators;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
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
import cz.nkp.differ.compare.io.generators.util.SortableButton;
import cz.nkp.differ.compare.metadata.ImageMetadata;
import cz.nkp.differ.compare.metadata.JP2ProfileValidationResult;
import cz.nkp.differ.compare.metadata.MetadataGroups;
import cz.nkp.differ.compare.metadata.ValidatedProperty;
import cz.nkp.differ.gui.windows.GlitchDetectorWindow;
import cz.nkp.differ.gui.windows.JP2ProfileValidationResultWindow;
import cz.nkp.differ.gui.windows.RawDataWindow;
import cz.nkp.differ.tools.GlossaryUtil;
import cz.nkp.differ.tools.HTMLGlossaryUtil;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author xrosecky
 */
public class ImageMetadataTableGenerator {

	private static final int TABLE_WIDTH = 417;
	private static final String COLUMN_KEY_PROPERTY = "key";
	private static final String COLUMN_SOURCE_PROPERTY = "source";
	private static final String COLUMN_VALUE_PROPERTY = "image value";
	private static final String COLUMN_UNIT_PROPERTY = "unit";
	private static final String COLUMN_CONFLICT_PROPERTY = "conflict";

	private ImageProcessorResult[] results;
	private CompareComponent parent;
	private GlossaryUtil glossaryUtil = DifferApplication.getGlossaryUtil();

	public ImageMetadataTableGenerator(ImageProcessorResult[] results,
			CompareComponent parent) {
		this.results = results;
		this.parent = parent;
	}

	public Layout getComponent() {
		final VerticalLayout layout = new VerticalLayout();
		layout.addStyleName("v-table-metadata");
		layout.setSpacing(true);
		@SuppressWarnings("unchecked")
		List<ImageMetadata>[] dataArray = (List<ImageMetadata>[]) Array
				.newInstance(List.class, results.length + 1);
		for (int i = 0; i != results.length; i++) {
			dataArray[i] = results[i].getMetadata();
		}
		HashMap<String, ComparedImagesMetadata> metadata = getMetadataTable(dataArray);
		MetadataGroups metadataGroups = DifferApplication.getMetadataGroups();
		Map<String, ComparedImagesMetadata> profileProps = filterBySource(
				"Profile validation", metadata);
		final Map<String, ComparedImagesMetadata> glitchProps = filterBySource(
				GlitchDetectorResultPostProcessor.SOURCE_NAME, metadata);
		layout.addComponent(generateMetadataTable(
				"Identification",
				filterByProperties(metadata,
						metadataGroups.getIdentificationProperties())));
		layout.addComponent(generateMetadataTable(
				"Validation",
				filterByProperties(metadata,
						metadataGroups.getValidationProperties())));
		layout.addComponent(generateMetadataTable(
				"Characterization",
				filterByProperties(metadata,
						metadataGroups.getCharacterizationProperties())));
		if (!profileProps.isEmpty()) {
			layout.addComponent(generateMetadataTable(
					"JPEG2000 profile validation", profileProps));
		}
		layout.addComponent(generateMetadataTable(
				"Others",
				filterByOtherProperties(metadata,
						metadataGroups.getAllProperties())));
		final GlitchDetectorConfig config = new GlitchDetectorConfig();
		if (!glitchProps.isEmpty()) {
			final Table table = generateMetadataTable(
					GlitchDetectorResultPostProcessor.SOURCE_NAME, glitchProps);
			layout.addComponent(table);
			final Button glitchSettingButton = new Button("settings");
			glitchSettingButton.addListener(new Button.ClickListener() {

				private Table newTable = null;

				@Override
				public void buttonClick(Button.ClickEvent event) {
					final GlitchDetectorWindow window = new GlitchDetectorWindow(
							config);
					window.setOnSubmit(new Button.ClickListener() {

						@Override
						public void buttonClick(Button.ClickEvent event) {
							if (newTable == null) {
								layout.removeComponent(table);
							} else {
								layout.removeComponent(newTable);
							}
							layout.removeComponent(glitchSettingButton);
							GlitchDetectorResultPostProcessor processor = DifferApplication
									.getApplicationContext()
									.getBean(
											GlitchDetectorResultPostProcessor.class);
							List<ImageMetadata>[] datas = (List<ImageMetadata>[]) new List[results.length + 1];
							int index = 0;
							for (ImageProcessorResult res : results) {
								datas[index] = processor.process(res, config);
								index++;
							}
							HashMap<String, ComparedImagesMetadata> newGlitchProps = getMetadataTable(datas);
							newTable = generateMetadataTable(
									GlitchDetectorResultPostProcessor.SOURCE_NAME,
									newGlitchProps);
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
		/*
		 * Table metadataTable = generateMetadataTable("test", metadata);
		 * layout.addComponent(metadataTable);
		 */
		return layout;
	}

	private HashMap<String, ComparedImagesMetadata> getMetadataTable(
			List<ImageMetadata>[] dataArray) {
		HashMap<String, ComparedImagesMetadata> hashmap = new HashMap<String, ComparedImagesMetadata>();
		for (int resultIndex = 0; resultIndex < dataArray.length; resultIndex++) {
			if (dataArray[resultIndex] == null) {
				continue;
			}
			for (ImageMetadata data : dataArray[resultIndex]) {
				String sourceName = "unknown";
				String version = null;
				if (data.getSource() != null
						&& data.getSource().getSourceName() != null) {
					sourceName = data.getSource().getSourceName();
					version = data.getSource().getVersion();
				}
				String id = data.getKey() + "&&" + sourceName;
				ComparedImagesMetadata cim = hashmap.get(id);
				if (cim == null) {
					cim = new ComparedImagesMetadata(id);
					cim.setKey(data.getKey());
					cim.setUnit(data.getUnit());
					cim.setConflict(data.isConflict());
					cim.setSourceName(sourceName);
					cim.setVersion(version);
					ImageMetadata[] metadata = new ImageMetadata[dataArray.length + 1];
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

	private Table generateMetadataTable(String group,
			Map<String, ComparedImagesMetadata> hashmap) {
		final Table metadataTable = new Table(group.toUpperCase());
		Object[] visibleColumns = new Object[results.length + 3];
		// header
		metadataTable.addContainerProperty(COLUMN_KEY_PROPERTY,
				SortableButton.class, null);
		visibleColumns[0] = COLUMN_KEY_PROPERTY;
		metadataTable.addContainerProperty(COLUMN_SOURCE_PROPERTY,
				SortableButton.class, null);
		visibleColumns[1] = COLUMN_SOURCE_PROPERTY;
		for (int i = 0; i != results.length; i++) {
			String name = COLUMN_VALUE_PROPERTY + " " + Integer.toString(i + 1);
			metadataTable
					.addContainerProperty(name, SortableButton.class, null);
			metadataTable.setColumnWidth(name, 200);
			visibleColumns[2 + i] = name;
		}
		metadataTable.addContainerProperty(COLUMN_UNIT_PROPERTY, String.class,
				null);
		visibleColumns[2 + results.length] = COLUMN_UNIT_PROPERTY;
		metadataTable.addContainerProperty(COLUMN_UNIT_PROPERTY, String.class,
				null);
		metadataTable.addContainerProperty(COLUMN_CONFLICT_PROPERTY,
				Boolean.class, null);
		// rows
		int row = 0;
		for (Map.Entry<String, ComparedImagesMetadata> entry : hashmap
				.entrySet()) {
			ComparedImagesMetadata cim = entry.getValue();
			Object key = createClickableProperty(cim.getKey());
			Button clickableToolName = createClickableTool(cim.getSourceName(),
					cim.getVersion());

			Object[] columns = new Object[results.length + 4];
			columns[0] = key;
			columns[1] = clickableToolName;
			for (int i = 0; i != results.length; i++) {
				columns[2 + i] = createClickableValue(cim.getImageMetadata()[i]);
			}
			columns[2 + results.length] = cim.getUnit();
			columns[3 + results.length] = cim.isConflict();
			metadataTable.addItem(columns, row);
			row++;
		}
		// styling
		metadataTable.setVisibleColumns(visibleColumns);
		metadataTable.setSelectable(true);
		metadataTable.setMultiSelect(false);
		metadataTable.setImmediate(true);
		metadataTable.setWidth(results.length * TABLE_WIDTH,
				Sizeable.UNITS_PIXELS);
		metadataTable.setPageLength(Math.min(row, 10));
		metadataTable.sort(new Object[] { COLUMN_KEY_PROPERTY },
				new boolean[] { true });
		if (group.equals("JPEG2000 profile validation")
				|| group.equals(GlitchDetectorResultPostProcessor.SOURCE_NAME)) {
			metadataTable
					.setCellStyleGenerator(new ValidatedPropertyCellStyleGenerator(
							metadataTable));
		} else {
			metadataTable.setCellStyleGenerator(new ConflictCellStyleGenerator(
					metadataTable));
		}
		return metadataTable;
	}

	private Map<String, ComparedImagesMetadata> filterByProperties(
			HashMap<String, ComparedImagesMetadata> metadata,
			Set<String> allowed) {
		Map<String, ComparedImagesMetadata> results = new HashMap<String, ComparedImagesMetadata>();
		for (Map.Entry<String, ComparedImagesMetadata> entry : metadata
				.entrySet()) {
			if (allowed.contains(entry.getValue().getKey())) {
				results.put(entry.getKey(), entry.getValue());
			}
		}
		return results;
	}

	private Map<String, ComparedImagesMetadata> filterByOtherProperties(
			HashMap<String, ComparedImagesMetadata> metadata,
			Set<String> allowed) {
		Map<String, ComparedImagesMetadata> results = new HashMap<String, ComparedImagesMetadata>();
		for (Map.Entry<String, ComparedImagesMetadata> entry : metadata
				.entrySet()) {
			if (!allowed.contains(entry.getValue().getKey())) {
				results.put(entry.getKey(), entry.getValue());
			}
		}
		return results;
	}

	private Map<String, ComparedImagesMetadata> filterBySource(
			String sourceName, HashMap<String, ComparedImagesMetadata> metadata) {
		Set<String> propertiesToRemove = new HashSet<String>();
		Map<String, ComparedImagesMetadata> results = new HashMap<String, ComparedImagesMetadata>();
		for (Map.Entry<String, ComparedImagesMetadata> entry : metadata
				.entrySet()) {
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

	private SortableButton createClickableProperty(final String propertyName) {
		SortableButton button = new SortableButton(propertyName, null);
		if (glossaryUtil.existsGlossaryFor(propertyName, Locale.US, "glossary")) {
			final String glossary = glossaryUtil.getGlossaryFor(propertyName,
					Locale.US, "glossary");
			button.addListener(new Button.ClickListener() {

				@Override
				public void buttonClick(Button.ClickEvent event) {
					parent.getMainWindow().showNotification(propertyName,
							"<br/>" + glossary,
							Window.Notification.TYPE_HUMANIZED_MESSAGE);
				}
			});
			button.addStyleName("link");
		} else {
			button.addStyleName("nolink");
		}
		return button;
	}

	private SortableButton createClickableTool(String source, String version) {
		final String toolName = (source == null || source.isEmpty()) ? "tool name unknown"
				: source;
		final String ver = (version == null || version.isEmpty()) ? "unknown"
				: version;
		SortableButton button = new SortableButton(toolName, null);
		button.addListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				parent.getMainWindow().showNotification(toolName,
						"<br/>version " + ver,
						Window.Notification.TYPE_HUMANIZED_MESSAGE);
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
				public void buttonClick(Button.ClickEvent event) {
					Window window = null;
					if (data.getData() == null) {
						window = new RawDataWindow(parent, data.getSource());
					}
					if (data.getData() instanceof JP2ProfileValidationResult) {
						window = new JP2ProfileValidationResultWindow(
								(JP2ProfileValidationResult) data.getData());
					}
					parent.getMainWindow().addWindow(window);
				}
			});
		}
		button.addStyleName("link");
		return button;
	}

	private class ConflictCellStyleGenerator implements
			Table.CellStyleGenerator {

		private Table metadataTable;

		public ConflictCellStyleGenerator(Table metadataTable) {
			this.metadataTable = metadataTable;
		}

		@Override
		public String getStyle(Object itemId, Object propertyId) {
			if (results.length == 2 && propertyId == null) {
				Button valA = (Button) metadataTable.getContainerProperty(
						itemId, COLUMN_VALUE_PROPERTY + " 1").getValue();
				Button valB = (Button) metadataTable.getContainerProperty(
						itemId, COLUMN_VALUE_PROPERTY + " 2").getValue();
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

	private static class ConflictCellFromMetadataStyleGenerator implements
			Table.CellStyleGenerator {

		private Table metadataTable;

		public ConflictCellFromMetadataStyleGenerator(Table metadataTable) {
			this.metadataTable = metadataTable;
		}

		@Override
		public String getStyle(Object itemId, Object propertyId) {
			if (propertyId == null) {
				Boolean conflict = (Boolean) metadataTable
						.getContainerProperty(itemId, COLUMN_CONFLICT_PROPERTY)
						.getValue();
				return (conflict) ? "red" : "green";
			} else {
				return "";
			}
		}

	}

	private static class ValidatedPropertyCellStyleGenerator implements
			Table.CellStyleGenerator {

		private Table metadataTable;

		public ValidatedPropertyCellStyleGenerator(Table metadataTable) {
			this.metadataTable = metadataTable;
		}

		@Override
		public String getStyle(Object itemId, Object propertyId) {
			if (itemId != null && propertyId != null) {
				Object prop = (Object) metadataTable.getContainerProperty(
						itemId, propertyId).getValue();
				if (prop != null && prop instanceof SortableButton) {
					SortableButton butt = (SortableButton) prop;
					if (butt.getData() instanceof ValidatedProperty) {
						ValidatedProperty property = (ValidatedProperty) butt
								.getData();
						return (property.isValid()) ? "green" : "red";
					}
				}
			}
			return "";
		}
	}

}
