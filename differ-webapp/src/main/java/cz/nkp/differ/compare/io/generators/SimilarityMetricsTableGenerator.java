package cz.nkp.differ.compare.io.generators;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import cz.nkp.differ.compare.io.ImageProcessorResult;
import cz.nkp.differ.compare.metadata.ImageMetadata;
import java.util.Arrays;

/**
 *
 * @author xrosecky
 */
public class SimilarityMetricsTableGenerator {

	private static final int TABLE_WIDTH = 408;
	private static final String COLUMN_KEY_PROPERTY = "key";
	private static final String COLUMN_SOURCE_PROPERTY = "source";
	private static final String COLUMN_UNIT_PROPERTY = "unit";
	private static final String COLUMN_VALUE_PROPERTY = "value";
	private static final String TABLE_NAME = "Similarity metrics".toUpperCase();
	private static final Object[] VISIBLE_COLUMNS = new Object[]{
		COLUMN_KEY_PROPERTY,
		COLUMN_SOURCE_PROPERTY,
		COLUMN_VALUE_PROPERTY,
		COLUMN_UNIT_PROPERTY
	};
	private final ImageProcessorResult result;

	public SimilarityMetricsTableGenerator(ImageProcessorResult result) {
		this.result = result;
	}

	public Layout getComponent() {
		BeanItemContainer metadataContainer =
			new BeanItemContainer<ImageMetadata>(ImageMetadata.class, result.getMetadata());
		final Table metadataTable = new Table(TABLE_NAME, metadataContainer);
		metadataTable.setVisibleColumns(VISIBLE_COLUMNS);
		metadataTable.setColumnWidth(COLUMN_VALUE_PROPERTY, 200);
		metadataTable.sort(new String[]{COLUMN_KEY_PROPERTY}, new boolean[]{true});
		metadataTable.setSelectable(true);
		metadataTable.setMultiSelect(false);
		metadataTable.setImmediate(true);
		metadataTable.setWidth(TABLE_WIDTH, Sizeable.UNITS_PIXELS);
		metadataTable.setPageLength(result.getMetadata().size());

		metadataTable.setCellStyleGenerator(new Table.CellStyleGenerator() {

			@Override
			public String getStyle(Object itemId, Object propertyId) {
				final ImageMetadata metadata;
				if (itemId instanceof Integer) {
					metadata = result.getMetadata().get((Integer) itemId);
				} else {
					metadata = (ImageMetadata) itemId;
				}
				String key = metadata.getKey();
				if (Arrays.asList("red", "blue", "green").contains(key)) {
					return key + "-channel";
				}
				return "";
			}
		});

		VerticalLayout layout = new VerticalLayout();
		layout.addStyleName("v-table-metadata");
		layout.setSpacing(true);
		layout.addComponent(metadataTable);
		return layout;
	}
}
