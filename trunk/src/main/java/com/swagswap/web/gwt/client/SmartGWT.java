package com.swagswap.web.gwt.client;

import java.util.LinkedHashMap;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.OperatorId;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.LinkItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.tile.TileGrid;
import com.smartgwt.client.widgets.tile.events.RecordClickEvent;
import com.smartgwt.client.widgets.tile.events.RecordClickHandler;
import com.smartgwt.client.widgets.viewer.DetailFormatter;
import com.smartgwt.client.widgets.viewer.DetailViewerField;
import com.smartgwt.client.widgets.viewer.DetailViewerRecord;
import com.swagswap.web.gwt.client.domain.LoginInfo;

public class SmartGWT implements EntryPoint {
	private LoginInfo loginInfo; //null if they're not logged in

	private final SmartGWTItemServiceWrapperAsync service = GWT
			.create(SmartGWTItemServiceWrapper.class);

	final TileGrid tileGrid = new TileGrid();

	private VStack boundFormVStack;

	private DynamicForm boundSwagForm;

	public void onModuleLoad() {
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL() + "SwagSwapGWT.html",
				new AsyncCallback<LoginInfo>() {
					public void onFailure(Throwable error) {
						GWT.log("", error);
					}

					public void onSuccess(LoginInfo result) {
						loginInfo = result;
						buildGUI();
					}
				});
	}
	
	public void buildGUI() {
		final VStack vStack = new VStack(20);
		vStack.setShowEdges(true);
		// get better exception handling
		setUncaughtExceptionHandler();

		vStack.setWidth100();
		addLoginLogoutPanel(vStack);
		HStack searchSortHStack= new HStack();
		final DynamicForm filterForm = addSearchPanel(searchSortHStack);
		final DynamicForm sortForm = addSortPanel(searchSortHStack);
		vStack.addMember(searchSortHStack);
//		HLayout hLayout = addFilterButton(vStack, filterForm, sortForm);
		addItemsPanel(vStack);
		addEditForm(vStack);

		RootPanel.get("gwt-tilegrid").add(vStack);
		vStack.draw();
	}

	private HLayout addFilterButton(final VStack vStack,
			final DynamicForm filterForm, final DynamicForm sortForm) {
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
		return hLayout;
	}

	private DynamicForm addSortPanel(final HStack hStack) {
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
		hStack.addMember(sortForm);
		return sortForm;
	}

	private DynamicForm addSearchPanel(final HStack hStack) {
		final DynamicForm filterForm = new DynamicForm();
		filterForm.setIsGroup(true);
		filterForm.setGroupTitle("Search");
		filterForm.setNumCols(6);
		filterForm.setDataSource(SmartGWTRPCDataSource.getInstance());
		filterForm.setAutoFocus(false);

		TextItem nameItem = new TextItem("name");
		SelectItem companyItem = new SelectItem("company");
		companyItem.setOperator(OperatorId.CONTAINS); // fuzzy search!
		companyItem.setAllowEmptyValue(true);

		filterForm.setFields(nameItem, companyItem);

		filterForm.addItemChangedHandler(new ItemChangedHandler() {
			public void onItemChanged(ItemChangedEvent event) {
				tileGrid.fetchData(filterForm.getValuesAsCriteria());
			}
		});

		hStack.addMember(filterForm);
		return filterForm;
	}

	private void addItemsPanel(final VStack vStack) {
		tileGrid.setTileWidth(150);
		tileGrid.setTileValueAlign("left");
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
		// commonNameField.setCellStyle("commonName");
		DetailViewerField company = new DetailViewerField("company");
		DetailViewerField ownerNickName = new DetailViewerField("ownerNickName");
		DetailViewerField averageRating = new DetailViewerField("averageRating");
		DetailViewerField numberOfRatings = new DetailViewerField(
				"numberOfRatings");
		DetailViewerField lastUpdated = new DetailViewerField("lastUpdated");
		// lifeSpanField.setCellStyle("lifeSpan");
		lastUpdated.setDetailFormatter(new DetailFormatter() {
			public String format(Object value, DetailViewerRecord record,
					DetailViewerField field) {
				return "Last Updated: " + value;
			}
		});

		// DetailViewerField statusField = new DetailViewerField("status");
		// statusField.setCellStyleHandler(new CellStyleHandler() {
		// public String execute(Object value, DetailViewerField field,
		// DetailViewerRecord record) {
		// if ("Endangered".equals(value)) {
		// return "endangered";
		// } else if ("Threatened".equals(value)) {
		// return "threatened";
		// } else if ("Not Endangered".equals(value)) {
		// return "notEndangered";
		// } else {
		// return null;
		// }
		// }
		// });

		tileGrid.setFields(pictureField, keyField, name, company,
				ownerNickName, averageRating, numberOfRatings, lastUpdated);

		vStack.addMember(tileGrid);
	}
	
	private void addLoginLogoutPanel(final VStack vStack) {
		HStack loginPanel = new HStack();
		loginPanel.setWidth100();
		loginPanel.setHeight(10);
		HStack logoutPanel = new HStack();
		logoutPanel.setWidth100();
		logoutPanel.setHeight(10);
		Anchor signInLink = new Anchor("Sign In");
		Anchor signOutLink = new Anchor("Sign Out");
		Label addLink = new Label("Add Swag");
		addLink.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				boundFormVStack.show();
				boundSwagForm.editNewRecord();
			}
		});

		if (loginInfo.isLoggedIn()) {
			signOutLink.setHref(loginInfo.getLogoutUrl());
			logoutPanel.addMember(signOutLink);
			Label welcomeLabel = new Label("Welcome: "
					+ loginInfo.getNickname());
			welcomeLabel.setWrap(false);
			logoutPanel.addMember(welcomeLabel);
			logoutPanel.addMember(addLink);
			vStack.addMember(logoutPanel);
		} else {
			signInLink.setHref(loginInfo.getLoginUrl());
			loginPanel.addMember(signInLink);
			loginPanel.addMember(addLink);
			vStack.addMember(loginPanel);
		}
	}
	
	private void addEditForm(VStack vStack) {
		boundFormVStack = new VStack();
		boundSwagForm = new DynamicForm();
		boundSwagForm.setNumCols(6);
		boundSwagForm.setDataSource(SmartGWTRPCDataSource.getInstance());
		boundSwagForm.setAutoFocus(false);

		TextItem nameItem = new TextItem("name");
		TextItem companyItem = new TextItem("company");
		TextItem descriptionItem = new TextItem("description");
		TextItem tag1Item = new TextItem("tag1");
		TextItem tag2Item = new TextItem("tag2");
		TextItem tag3Item = new TextItem("tag3");
		TextItem tag4Item = new TextItem("tag4");

		boundSwagForm.setFields(nameItem, companyItem, descriptionItem, tag1Item,
				tag2Item, tag3Item, tag4Item);
		boundFormVStack.addMember(boundSwagForm);

		tileGrid.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				boundFormVStack.show();
				boundSwagForm.editRecord(event.getRecord());
			}
		});
		//
		// HLayout hLayout = new HLayout(10);
		// hLayout.setHeight(22);

		IButton saveButton = new IButton("Save");
		saveButton.setAutoFit(true);
		saveButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				boundSwagForm.saveData();
				if (boundSwagForm.hasErrors()) {
					Window.alert("" + boundSwagForm.getErrors());
				} else {
					boundSwagForm.clearValues();
					boundFormVStack.hide();
				}
			}
		});
		IButton cancelButton = new IButton("Cancel");
		cancelButton.setAutoFit(true);
		cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				boundSwagForm.clearValues();
				boundFormVStack.hide();
			}
		});
		HLayout hLayout = new HLayout();
		hLayout.addMember(saveButton);
		hLayout.addMember(cancelButton);
		boundFormVStack.addMember(hLayout);
		boundFormVStack.hide();
		vStack.addMember(boundFormVStack);
	}

	private void setUncaughtExceptionHandler() {
		// better exception handling
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void onUncaughtException(Throwable e) {
				if (e.getCause() != null
						&& e.getCause() instanceof StatusCodeException) {
					GWT.log("Exception (server-side): ", e);
					Window.alert("Exception (server-side): " + e.getMessage());
					e.printStackTrace();
				} else {
					GWT.log("Exception (client-side): ", e);
					Window.alert("Exception (client-side): " + e.getMessage());
					e.printStackTrace();
				}
			}
		});
	}

	// public void onClick(ClickEvent event) {
	// TileRecord record = tileGrid.getSelectedRecord();
	// PopupPanel popupPanel = new PopupPanel(true);
	// popupPanel.add(new T)
	// dialog.setAutoCenter(true);
	// dialog.animateShow(AnimationEffect.FADE, null, 1000);
	// }
}
