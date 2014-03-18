package cz.nkp.differ.gui.tabs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import cz.nkp.differ.DifferApplication;
import cz.nkp.differ.compare.io.CompareComponent;
import cz.nkp.differ.compare.io.ImageProcessorResult;
import cz.nkp.differ.compare.io.SerializableImageProcessorResults;
import cz.nkp.differ.example.Example;
import cz.nkp.differ.example.ExampleProvider;
import cz.nkp.differ.example.ExampleProviderImpl;
import cz.nkp.differ.gui.components.ProgressBarComponent;
import cz.nkp.differ.gui.windows.MainDifferWindow;
import cz.nkp.differ.model.Image;
import cz.nkp.differ.model.Result;

public class ExamplesTab extends HorizontalLayout {

	private static final long serialVersionUID = 1L;

	private MainDifferWindow mainWindow;
	
	private Layout customViewWrapper;
	private Layout topPanelWithButtons;
	
	public ExamplesTab(MainDifferWindow window) {
		this.mainWindow = window;
		this.setDefaultView();
	}
	
	public void setDefaultView() {
		HorizontalLayout mainLayout = new HorizontalLayout();
		this.addComponent(mainLayout);
		ExampleProvider exampleProvider = DifferApplication.getExampleProvider();
		List<Example> results = exampleProvider.getExamples();
		BeanItemContainer<Example> resultContainer = new BeanItemContainer<Example>(Example.class, results);
		final Table resultTable = new Table("results", resultContainer);
		resultTable.setVisibleColumns(new Object[] {"name"});
		resultTable.setSelectable(true);
		resultTable.setImmediate(true);
		resultTable.setMultiSelect(false);
		resultTable.setEditable(false);
		mainLayout.addComponent(resultTable);
		
		VerticalLayout buttonPanelRoot = new VerticalLayout();
		Panel panel = new Panel();
		panel.addComponent(buttonPanelRoot);
		mainLayout.addComponent(panel);
		Button showButton = new Button();
		showButton.setCaption("show");
		showButton.addListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (resultTable.getValue() != null) {
					Example example = (Example) resultTable.getValue();
					if (example != null) {
						try {
							HorizontalLayout layout = new HorizontalLayout();
							
							Image image1 = new Image();
						    image1.setFile(example.getFile1());
						    image1.setFileName(example.getFile1().getName());
						    
						    Image image2 = new Image();
						    image2.setFile(example.getFile2());
						    image2.setFileName(example.getFile2().getName());
							
						    Image[] selectedImages = new Image[]{image1, image2};
						    
						    CompareComponent cp = new CompareComponent(ExamplesTab.this.mainWindow);
						    layout.addComponent(new ProgressBarComponent(null, DifferApplication.getCurrentApplication(), cp, selectedImages));
						    ExamplesTab.this.setCustomView(layout, cp.getButtons());
							/*
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
							ExamplesTab.this.setCustomView(layout,
									compareComponent.getButtons());
									*/
						} catch (Exception ex) {
							throw new RuntimeException(ex);
						}
					}
				}
			}
		});
		panel.addComponent(showButton);
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
	
	private Button.ClickListener customViewWrapperBackButtonListener = new Button.ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			ExamplesTab.this.removeAllComponents();
			ExamplesTab.this.setDefaultView();
		}
	};

}
