package com.swagswap.web.gwt.client;

import java.util.LinkedHashMap;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
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
import com.smartgwt.client.widgets.tile.events.RecordClickEvent;
import com.smartgwt.client.widgets.tile.events.RecordClickHandler;
import com.smartgwt.client.widgets.viewer.DetailFormatter;
import com.smartgwt.client.widgets.viewer.DetailViewerField;
import com.smartgwt.client.widgets.viewer.DetailViewerRecord;

public class SmartGWT implements EntryPoint {
	// private final ItemServiceGWTWrapperAsync itemService = GWT
	// .create(ItemServiceGWTWrapper.class);
	private final SmartGWTItemServiceWrapperAsync service = GWT
			.create(SmartGWTItemServiceWrapper.class);

	final TileGrid tileGrid = new TileGrid();
	
	public void onModuleLoad() {

		VStack vStack = new VStack(20);
		vStack.setWidth100();

		
		tileGrid.setTileWidth(150);
		tileGrid.setTileHeight(205);
		tileGrid.setHeight(400);
		tileGrid.setShowAllRecords(true);
		tileGrid.setDataSource(SmartGWTRPCDataSource.getInstance());
		tileGrid.setAutoFetchData(true);
		tileGrid.setAnimateTileChange(true);

		DetailViewerField pictureField = new DetailViewerField("imageKey"); 
		pictureField.setImageWidth(150);
		pictureField.setImageHeight(125);
		pictureField.setCellStyle("picture");

		DetailViewerField keyField = new DetailViewerField("key");
		DetailViewerField name = new DetailViewerField("name");
//		commonNameField.setCellStyle("commonName");
		DetailViewerField company = new DetailViewerField("company");
		DetailViewerField ownerNickName = new DetailViewerField("ownerNickName");
		DetailViewerField averageRating = new DetailViewerField("averageRating");
		DetailViewerField numberOfRatings = new DetailViewerField("numberOfRatings");
		DetailViewerField lastUpdated = new DetailViewerField("lastUpdated");
//		lifeSpanField.setCellStyle("lifeSpan");
		lastUpdated.setDetailFormatter(new DetailFormatter() {
			public String format(Object value, DetailViewerRecord record,
					DetailViewerField field) {
				return "Last Updated: " + value;
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
		
		tileGrid.setFields(pictureField, keyField, name, company, ownerNickName, averageRating, numberOfRatings, lastUpdated);

		vStack.addMember(tileGrid);
		


		final DynamicForm filterForm = new DynamicForm();
		filterForm.setIsGroup(true);
		filterForm.setGroupTitle("Search");
		filterForm.setNumCols(6);
		filterForm.setDataSource(SmartGWTRPCDataSource.getInstance());
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
		statusItem.setOperator(OperatorId.CONTAINS); //fuzzy search!
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

		//form
		final DynamicForm boundForm = new DynamicForm();
		boundForm.setNumCols(6);
		boundForm.setDataSource(SmartGWTRPCDataSource.getInstance());
		boundForm.setAutoFocus(false);

		TextItem nameItem = new TextItem("name");
		TextItem companyItem = new TextItem("company");

		boundForm.setFields(nameItem, companyItem);

		vStack.addMember(boundForm);

		tileGrid.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				boundForm.editRecord(event.getRecord());
			}
		});
//
//		HLayout hLayout = new HLayout(10);
//		hLayout.setHeight(22);

		IButton button = new IButton("Save");
		button.setAutoFit(true);
		button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				boundForm.saveData();
				boundForm.clearValues();
			}
		});
		hLayout.addMember(button);
		vStack.addMember(hLayout);
		
	    RootPanel.get("gwt-tilegrid").add(vStack);
		vStack.draw();
	}

//	public void onClick(ClickEvent event) {
//		TileRecord record = tileGrid.getSelectedRecord();
//		PopupPanel popupPanel = new PopupPanel(true);
//		popupPanel.add(new T)
//		dialog.setAutoCenter(true);
//		dialog.animateShow(AnimationEffect.FADE, null, 1000);
//	}
}
