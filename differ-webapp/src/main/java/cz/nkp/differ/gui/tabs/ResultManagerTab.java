package cz.nkp.differ.gui.tabs;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.compare.io.CompareComponent;
import cz.nkp.differ.compare.io.ImageProcessorResult;
import cz.nkp.differ.compare.io.SerializableImageProcessorResults;
import cz.nkp.differ.gui.windows.MainDifferWindow;
import cz.nkp.differ.io.ResultManager;
import cz.nkp.differ.model.Result;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author xrosecky
 */
public class ResultManagerTab extends HorizontalLayout {

	private static final String[] VISIBLE_COLUMNS = new String[] { "name",
			"created", "shared" };

	private MainDifferWindow mainWindow;
	private boolean anonymous;
	private Table resultTable;
	private BeanItemContainer<Result> resultContainer = null;
	private Button showButton;
	private Button reloadButton;
	private Button removeButton;
	private ResultManager resultManager;
	private Layout customViewWrapper;
	private Layout topPanelWithButtons;

	public ResultManagerTab(MainDifferWindow window, boolean anonymous) {
		this.mainWindow = window;
		this.anonymous = anonymous;
		resultManager = DifferApplication.getResultManager();
		this.setDefaultView();
	}

	public void setDefaultView() {
		HorizontalLayout mainLayout = new HorizontalLayout();
		this.addComponent(mainLayout);
		List<Result> results;
		if (anonymous) {
			results = resultManager.getSharedResults();
		} else {
			results = resultManager.getResults();
		}
		resultContainer = new BeanItemContainer<Result>(Result.class, results);
		resultTable = new Table("results", resultContainer);
		resultTable.setVisibleColumns(VISIBLE_COLUMNS);
		resultTable.setColumnHeaders(VISIBLE_COLUMNS);
		resultTable.setTableFieldFactory(new DefaultFieldFactory() {

			@Override
			public Field createField(Container container, Object itemId,
					Object propertyId, Component uiContext) {
				Field field = super.createField(container, itemId, propertyId,
						uiContext);
				final Result result = (Result) itemId;
				boolean readOnly = anonymous;
				if (propertyId.equals("shared")) {
					CheckBox checkBox = new CheckBox();
					checkBox.setReadOnly(readOnly);
					checkBox.setDescription(null);
					checkBox.setImmediate(true);
					checkBox.addListener(new Property.ValueChangeListener() {

						@Override
						public void valueChange(Property.ValueChangeEvent event) {
							ResultManagerTab.this.resultManager.update(result);
						}
					});
					return checkBox;
				}
				field.setHeight(1.5f, UNITS_EM);
				field.setReadOnly(true);
				return field;
			}
		});
		resultTable.setSelectable(true);
		resultTable.setImmediate(true);
		resultTable.setMultiSelect(false);
		resultTable.setEditable(true);
		mainLayout.addComponent(resultTable);
		VerticalLayout buttonPanelRoot = new VerticalLayout();
		Panel panel = new Panel();
		panel.addComponent(buttonPanelRoot);
		mainLayout.addComponent(panel);
		showButton = new Button();
		showButton.setCaption("show");
		showButton.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (resultTable.getValue() != null) {
					Result result = (Result) resultTable.getValue();
					if (result != null) {
						try {
							HorizontalLayout layout = new HorizontalLayout();
							SerializableImageProcessorResults resultsToShow = resultManager
									.getResult(result);
							ArrayList<ImageProcessorResult> results = new ArrayList<ImageProcessorResult>();
							results.addAll(resultsToShow.getResults());
							ImageProcessorResult[] asArray = new ImageProcessorResult[results
									.size()];
							results.toArray(asArray);
							CompareComponent compareComponent = new CompareComponent(
									mainWindow, asArray);
							layout.addComponent(compareComponent
									.getPluginDisplayComponent());
							ResultManagerTab.this.setCustomView(layout,
									compareComponent.getButtons());
						} catch (Exception ex) {
							throw new RuntimeException(ex);
						}
					}
				}
			}
		});
		panel.addComponent(showButton);

		if (!anonymous) {
			removeButton = new Button();
			removeButton.setCaption("delete");
			removeButton.addListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					if (resultTable.getValue() != null) {
						Result result = (Result) resultTable.getValue();
						if (result != null) {
							resultManager.delete(result);
							ResultManagerTab.this.reload();
						}
					}
				}
			});
			panel.addComponent(removeButton);
		}

		reloadButton = new Button();
		reloadButton.setCaption("reload");
		reloadButton.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				ResultManagerTab.this.reload();
			}
		});
		panel.addComponent(reloadButton);
		this.setSizeUndefined();
	}

	public void setCustomView(Layout layout, List<Button> buttons) {
		if (customViewWrapper == null) {
			customViewWrapper = new VerticalLayout();
			topPanelWithButtons = new HorizontalLayout();
			Button backButton = new Button("Back");
			backButton.addListener(customViewWrapperBackButtonListener);
			topPanelWithButtons.addComponent(backButton);
			for (Button button : buttons) {
				topPanelWithButtons.addComponent(button);
			}
		}
		customViewWrapper.removeAllComponents();
		customViewWrapper.addComponent(topPanelWithButtons);
		customViewWrapper.addComponent(layout);
		customViewWrapper.setSizeUndefined();
		this.removeAllComponents();
		this.addComponent(customViewWrapper);
		this.setSizeUndefined();
	}

	private void reload() {
		removeAllComponents();
		setDefaultView();
	}

	private Button.ClickListener customViewWrapperBackButtonListener = new Button.ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			ResultManagerTab.this.removeAllComponents();
			ResultManagerTab.this.setDefaultView();
		}
	};
}
