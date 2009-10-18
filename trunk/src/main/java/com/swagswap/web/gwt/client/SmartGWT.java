package com.swagswap.web.gwt.client;

import java.util.LinkedHashMap;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.OperatorId;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SliderItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.tile.TileGrid;
import com.smartgwt.client.widgets.viewer.CellStyleHandler;
import com.smartgwt.client.widgets.viewer.DetailFormatter;
import com.smartgwt.client.widgets.viewer.DetailViewerField;
import com.smartgwt.client.widgets.viewer.DetailViewerRecord;

public class SmartGWT implements EntryPoint {
	// private final ItemServiceGWTWrapperAsync itemService = GWT
	// .create(ItemServiceGWTWrapper.class);
	private final SimpleGwtRPCDSServiceAsync service = GWT
			.create(SimpleGwtRPCDSService.class);

	public void onModuleLoad() {

		VStack vStack = new VStack(20);
		vStack.setWidth100();

		final TileGrid tileGrid = new TileGrid();
		tileGrid.setTileWidth(150);
		tileGrid.setTileHeight(205);
		tileGrid.setHeight(400);
		tileGrid.setShowAllRecords(true);
		tileGrid.setDataSource(SimpleGwtRPCDataSource.getInstance());
		tileGrid.setAutoFetchData(true);
		tileGrid.setAnimateTileChange(true);

		DetailViewerField pictureField = new DetailViewerField("picture"); 
		DetailViewerField idField = new DetailViewerField("id");
		DetailViewerField commonNameField = new DetailViewerField("name");
		commonNameField.setCellStyle("commonName");

		DetailViewerField lifeSpanField = new DetailViewerField("date");
		lifeSpanField.setCellStyle("lifeSpan");
		lifeSpanField.setDetailFormatter(new DetailFormatter() {
			public String format(Object value, DetailViewerRecord record,
					DetailViewerField field) {
				return "date: " + value;
			}
		});

//		DetailViewerField statusField = new DetailViewerField("status");
//		statusField.setCellStyleHandler(new CellStyleHandler() {
//			public String execute(Object value, DetailViewerField field,
//					DetailViewerRecord record) {
//				if ("Endangered".equals(value)) {
//					return "endangered";
//				} else if ("Threatened".equals(value)) {
//					return "threatened";
//				} else if ("Not Endangered".equals(value)) {
//					return "notEndangered";
//				} else {
//					return null;
//				}
//			}
//		});
		
		tileGrid.setFields(pictureField, idField, commonNameField, lifeSpanField);

		vStack.addMember(tileGrid);

		final DynamicForm filterForm = new DynamicForm();
		filterForm.setIsGroup(true);
		filterForm.setGroupTitle("Search");
		filterForm.setNumCols(6);
		filterForm.setDataSource(SimpleGwtRPCDataSource.getInstance());
		filterForm.setAutoFocus(false);

		TextItem commonNameItem = new TextItem("name");
		SliderItem lifeSpanItem = new SliderItem("id");
		lifeSpanItem.setTitle("Max Life Span");
		lifeSpanItem.setMinValue(1);
		lifeSpanItem.setMaxValue(60);
		lifeSpanItem.setDefaultValue(60);
		lifeSpanItem.setHeight(50);
		lifeSpanItem.setOperator(OperatorId.LESS_THAN);

		SelectItem statusItem = new SelectItem("name");
		statusItem.setOperator(OperatorId.EQUALS);
		statusItem.setAllowEmptyValue(true);

		filterForm.setFields(commonNameItem, lifeSpanItem, statusItem);

		filterForm.addItemChangedHandler(new ItemChangedHandler() {
			public void onItemChanged(ItemChangedEvent event) {
				tileGrid.fetchData(filterForm.getValuesAsCriteria());
			}
		});

		vStack.addMember(filterForm);

		final DynamicForm sortForm = new DynamicForm();
		sortForm.setIsGroup(true);
		sortForm.setGroupTitle("Sort");
		sortForm.setAutoFocus(false);
		sortForm.setNumCols(6);

		SelectItem sortItem = new SelectItem();
		sortItem.setName("sortBy");
		sortItem.setTitle("Sort By");

		LinkedHashMap valueMap = new LinkedHashMap();
		valueMap.put("id", "Id");
		valueMap.put("name", "Name");
		valueMap.put("date", "Date");

		sortItem.setValueMap(valueMap);

		final CheckboxItem ascendingItem = new CheckboxItem("chkSortDir");
		ascendingItem.setTitle("Ascending");

		sortForm.setFields(sortItem, ascendingItem);

		sortForm.addItemChangedHandler(new ItemChangedHandler() {
			public void onItemChanged(ItemChangedEvent event) {
				String sortVal = sortForm.getValueAsString("sortBy");
				Boolean sortDir = (Boolean) ascendingItem.getValue();
				if (sortDir == null)
					sortDir = false;
				if (sortVal != null) {
					tileGrid.sortByProperty(sortVal, sortDir);
				}
			}
		});
		vStack.addMember(sortForm);

		HLayout hLayout = new HLayout(10);
		hLayout.setHeight(22);

		IButton filterButton = new IButton("Filter");
		filterButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				tileGrid.fetchData(filterForm.getValuesAsCriteria());
			}
		});
		filterButton.setAutoFit(true);

		IButton clearButton = new IButton("Clear");
		clearButton.setAutoFit(true);
		clearButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				tileGrid.fetchData();
				filterForm.clearValues();
				sortForm.clearValues();
			}
		});

		hLayout.addMember(filterButton);
		hLayout.addMember(clearButton);
		vStack.addMember(hLayout);

		vStack.draw();
	}

}
