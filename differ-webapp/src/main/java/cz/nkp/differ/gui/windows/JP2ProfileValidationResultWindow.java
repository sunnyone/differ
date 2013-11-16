package cz.nkp.differ.gui.windows;

import com.vaadin.ui.Table;
import java.util.List;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import cz.nkp.differ.compare.metadata.JP2Metadata;
import cz.nkp.differ.compare.metadata.JP2Profile;
import cz.nkp.differ.compare.metadata.JP2ProfileValidationResult;

/**
 *
 * @author xrosecky
 */
public class JP2ProfileValidationResultWindow extends Window {

    private static final String TABLE_NAME = "JPEG2000 profile validation";
    private static final String PROPERTY_COLUMN_NAME = "property";
    private static final String PROPERTY_COLUMN_VALUE = "value";
    private static final String PROPERTY_COLUMN_PROFILE = "profile";
    private static final int TABLE_WIDTH = 720;

    public JP2ProfileValidationResultWindow(JP2ProfileValidationResult result) {
	setCaption("JPEG2000 profile validation");
	setModal(true);
	setDraggable(false);
	setResizable(false);
	center();
	setWidth("800px");
	setHeight("100%");
	VerticalLayout windowLayout = new VerticalLayout();
	Table metadataTable = generateMetadataTable(result);
	windowLayout.addComponent(metadataTable);
	addComponent(windowLayout);
    }

    private Table generateMetadataTable(JP2ProfileValidationResult result) {
	JP2Metadata metadata = result.getMetadata();
	JP2Profile profile = result.getProfile();

	final Table metadataTable = new Table(TABLE_NAME);
	metadataTable.addContainerProperty(PROPERTY_COLUMN_NAME, String.class, null);
        metadataTable.addContainerProperty(PROPERTY_COLUMN_VALUE, String.class, null);
        metadataTable.addContainerProperty(PROPERTY_COLUMN_PROFILE, String.class, null);

	final boolean conflicts[] = new boolean[10];

	String[] decompositionLevel = new String[] {
	    "decomposition level",
	    Integer.toString(metadata.getDecompositionLevel()),
	    format(profile.getDecompositionLevels()),
	};
	metadataTable.addItem(decompositionLevel, 0);
	conflicts[0] = result.isDecompositionLevel();

	Object[] qualityLayers = new Object[] {
	    "quality layers",
	    Integer.toString(metadata.getQualityLayers()),
	    String.format("%s - %s", profile.getMinQualityLayers(), profile.getMaxQualityLayers()),
	};
	metadataTable.addItem(qualityLayers, 1);
	conflicts[1] = result.isQualityLayers();

	Object[] kernel = new Object[] {
	    "kernel",
	    toString(metadata.getKernel()),
	    toString(profile.getKernel()),
	};
	metadataTable.addItem(kernel, 2);
	conflicts[2] = result.isKernel();

	Object[] preccints = new Object[] {
	    "preccint sizes",
	    format(metadata.getPreccintSizes()),
	    format(profile.getPreccintSizes()),
	};
	metadataTable.addItem(preccints, 3);
	conflicts[3] = result.isPreccintSizes();

	Object[] progressionOrder = new Object[] {
	    "progression order",
	    toString(metadata.getProgressionOrder()),
	    format(profile.getProgressionOrders()),
	};
	metadataTable.addItem(progressionOrder, 4);
	conflicts[4] = result.isProgressionOrder();

	Object[] tileSize = new Object[] {
	    "tile size",
	    toString(metadata.getTileSize()),
	    format(profile.getTileSizes()),
	};
	metadataTable.addItem(tileSize, 5);
	conflicts[5] = result.isTileSize();

	metadataTable.setWidth(TABLE_WIDTH, Sizeable.UNITS_PIXELS);
	metadataTable.setPageLength(10);
	metadataTable.setImmediate(true);

	metadataTable.setCellStyleGenerator(new Table.CellStyleGenerator() {
            @Override
            public String getStyle(Object itemId, Object propertyId) {
		String style = null;
		if (itemId instanceof Integer) {
		    style = (conflicts[(Integer) itemId]) ? "green" : "red";
		}
		return style;
            }
        });

	return metadataTable;
    }

    private <T> String format(List<T> values) {
	StringBuilder result = new StringBuilder();
	String sep = "";
	for (T value : values) {
	    result.append(sep);
	    result.append(value.toString());
	    sep = ",";
	}
	return result.toString();
    }

    private String toString(Object value) {
	return (value == null)? "" : value.toString();
    }

}
