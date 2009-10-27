package com.swagswap.web.gwt.client;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ImageStyle;
import com.smartgwt.client.types.OperatorId;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.tile.TileGrid;
import com.smartgwt.client.widgets.tile.TileRecord;
import com.smartgwt.client.widgets.tile.events.RecordClickEvent;
import com.smartgwt.client.widgets.tile.events.RecordClickHandler;
import com.smartgwt.client.widgets.viewer.DetailFormatter;
import com.smartgwt.client.widgets.viewer.DetailViewerField;
import com.smartgwt.client.widgets.viewer.DetailViewerRecord;
import com.swagswap.domain.SwagItemRating;
import com.swagswap.web.gwt.client.domain.LoginInfo;
import com.swagswap.web.gwt.client.domain.SwagItemGWTDTO;

public class SmartGWT implements EntryPoint {

	private LoginInfo loginInfo; //null if they're not logged in

	private final SmartGWTItemServiceWrapperAsync service = GWT
			.create(SmartGWTItemServiceWrapper.class);

	final TileGrid tileGrid = new TileGrid();

	private VStack boundFormVStack;
	private DynamicForm boundSwagForm;

	private LoginServiceAsync loginService = GWT.create(LoginService.class);

	private List<Img> starImages = new ArrayList<Img>();

	private Img currentSwagImage;

	private StarClickHandler starClickHandler1;
	private StarClickHandler starClickHandler2;
	private StarClickHandler starClickHandler3;
	private StarClickHandler starClickHandler4;
	private StarClickHandler starClickHandler5;

	private HStack starHStack;
	
	public void onModuleLoad() {
		// get better exception handling
		setUncaughtExceptionHandler();
		
		loginService.login(GWT.getHostPageBaseURL() + "SwagSwapGWT.html",
			new AsyncCallback<LoginInfo>() {
				public void onFailure(Throwable error) {
					GWT.log("", error);
				}
				public void onSuccess(LoginInfo result) {
					loginInfo = result;
					buildGUI();
					//TODO check if tileRecord is still valid before doing this
//					TileRecord currentTileRecord = getCurrentTileRecord(); 
//					if (currentTileRecord!=null) {
//						prepareAndShowEditForm(currentTileRecord); 
//					}
				}
				
				/**
				 * For showing last viewed record if any
				 * @return currentSwagItem from session or null
				 */
				private TileRecord getCurrentTileRecord() {
					SwagItemGWTDTO currentSwagItemGWTDTO = loginInfo.getCurrentSwagItem();
					if (currentSwagItemGWTDTO==null) {
						return null;
					}
					TileRecord currentTileRecord = new TileRecord();
					SmartGWTRPCDataSource.copyValues(currentSwagItemGWTDTO,currentTileRecord);
					return currentTileRecord;
				}
			});
		
	}
	
	public void buildGUI() {
		final VStack vStack = new VStack(20);
		vStack.setShowEdges(true);
		vStack.setWidth100();
		addLoginLogoutPanel(vStack);
		HStack searchSortHStack= new HStack();
		searchSortHStack.setHeight(25);
		final DynamicForm filterForm = addSearchPanel(searchSortHStack);
		final DynamicForm sortForm = addSortPanel(searchSortHStack);
		vStack.addMember(searchSortHStack);
//		HLayout hLayout = addFilterButton(vStack, filterForm, sortForm);
		HStack itemsAndEditHStack = new HStack();
		addItemsPanel(itemsAndEditHStack);
		addEditForm(itemsAndEditHStack);
		vStack.addMember(itemsAndEditHStack);

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

	private void addItemsPanel(final HStack hStack) {
		tileGrid.setTileWidth(100);
		tileGrid.setTileHeight(140);
		tileGrid.setTileValueAlign("left");
		tileGrid.setWidth(600);
		tileGrid.setHeight(600);
		tileGrid.setShowResizeBar(true);
		tileGrid.setShowAllRecords(true);
		tileGrid.setDataSource(SmartGWTRPCDataSource.getInstance());
		tileGrid.setAutoFetchData(true);
		tileGrid.setAnimateTileChange(true);

		DetailViewerField pictureField = new DetailViewerField("imageKey");
		pictureField.setImageWidth(62);
		pictureField.setImageHeight(50);
		pictureField.setCellStyle("picture");

		DetailViewerField name = new DetailViewerField("name");
		// commonNameField.setCellStyle("commonName");
		DetailViewerField company = new DetailViewerField("company");
		DetailViewerField ownerNickName = new DetailViewerField("ownerNickName");
		DetailViewerField averageRating = new DetailViewerField("averageRating");
		averageRating.setDetailFormatter(new DetailFormatter() {
			public String format(Object value, DetailViewerRecord record,
					DetailViewerField field) {
					int averageRating = record.getAttributeAsInt("averageRating");
					int numberOfRatings = record.getAttributeAsInt("numberOfRatings");
					
				return createStarsHTMLString(averageRating) + " / " + numberOfRatings;
			}
		});
		DetailViewerField lastUpdated = new DetailViewerField("lastUpdated");
		tileGrid.setFields(pictureField, name, company ,ownerNickName, averageRating, lastUpdated);
		hStack.addMember(tileGrid);
	}
	
	public String createStarsHTMLString(float rating) {
		int roundedRating = Math.round(rating);
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			s.append((i<roundedRating)?"<img src=\"images/starOn.gif\"/>":"<img src=\"images/starOff.gif\"/>");
		}
		return s.toString();
	}
	
	private void addLoginLogoutPanel(final VStack vStack) {
		HStack loginPanel = new HStack();
		loginPanel.setWidth100();
		loginPanel.setHeight(10);
		HStack logoutPanel = new HStack();
		logoutPanel.setWidth100();
		logoutPanel.setHeight(10);
		logoutPanel.setPadding(20);
		Anchor signInLink = new Anchor("Sign In");
		Anchor signOutLink = new Anchor("Sign Out");
		Label addLink = new Label(" Add Swag");
		addLink.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				currentSwagImage.setSrc("/images/no_photo.jpg");
				boundFormVStack.hideMember(starHStack);
				boundFormVStack.show();
				boundSwagForm.editNewRecord();
			}
		});

		if (loginInfo.isLoggedIn()) {
			signOutLink.setHref(loginInfo.getLogoutUrl());
			logoutPanel.addMember(signOutLink);
			Label welcomeLabel = new Label("Welcome: "
					+ loginInfo.getNickName());
			welcomeLabel.setWrap(false);
			logoutPanel.addMember(welcomeLabel);
			logoutPanel.addMember(addLink);
			vStack.addMember(logoutPanel);
		} else { //not logged in
			signInLink.setHref(loginInfo.getLoginUrl());
			loginPanel.addMember(signInLink);
			vStack.addMember(loginPanel);
		}
	}
	
	/**
	 * For adding or editing a swagItem
	 */
	private void addEditForm(HStack hStack) {
		boundFormVStack = new VStack();
		boundSwagForm = new DynamicForm();
		boundSwagForm.setNumCols(2);
		boundSwagForm.setDataSource(SmartGWTRPCDataSource.getInstance());
		boundSwagForm.setAutoFocus(false);

		//add five stars
		for (int i = 0; i < 5; i++) {
			starImages.add(new Img("/images/starOff.gif",12,12));
		}
		
		starClickHandler1 = new StarClickHandler(1);
		starClickHandler2 = new StarClickHandler(2);
		starClickHandler3 = new StarClickHandler(3);
		starClickHandler4 = new StarClickHandler(4);
		starClickHandler5 = new StarClickHandler(5);
		
		starImages.get(0).addClickHandler(starClickHandler1);
		starImages.get(1).addClickHandler(starClickHandler2);
		starImages.get(2).addClickHandler(starClickHandler3);
		starImages.get(3).addClickHandler(starClickHandler4);
		starImages.get(4).addClickHandler(starClickHandler5);
		
		starHStack = new HStack();
//		starHStack.setBorder("2px solid blue");
		starHStack.setHeight(25);
		starHStack.setAlign(Alignment.RIGHT);
		starHStack.addMember(new Label("My Rating: "));
		starHStack.addMember(starImages.get(0));
		starHStack.addMember(starImages.get(1));
		starHStack.addMember(starImages.get(2));
		starHStack.addMember(starImages.get(3));
		starHStack.addMember(starImages.get(4));
		boundFormVStack.addMember(starHStack);
		
		//end stars
		
		TextItem nameItem = new TextItem("name");
		TextItem companyItem = new TextItem("company");
		TextItem descriptionItem = new TextItem("description");
		TextItem tag1Item = new TextItem("tag1");
		TextItem tag2Item = new TextItem("tag2");
		TextItem tag3Item = new TextItem("tag3");
		TextItem tag4Item = new TextItem("tag4");
		
		
		StaticTextItem isFetchOnlyItem = new StaticTextItem("isFetchOnly");
		isFetchOnlyItem.setVisible(false);
		StaticTextItem imageKeyItem = new StaticTextItem("imageKey");
		imageKeyItem.setVisible(false);

		boundSwagForm.setFields(nameItem, companyItem, descriptionItem, tag1Item,
				tag2Item, tag3Item, tag4Item, isFetchOnlyItem, imageKeyItem);
		boundFormVStack.addMember(boundSwagForm);
		
		currentSwagImage = new Img("/images/no_photo.jpg");  
		currentSwagImage.setImageType(ImageStyle.NORMAL); 
		boundFormVStack.addMember(currentSwagImage);
		
		tileGrid.addRecordClickHandler(new RecordClickHandler() {

			public void onRecordClick(RecordClickEvent event) {
				SwagItemGWTDTO dto = new SwagItemGWTDTO();
				SmartGWTRPCDataSource.copyValues(event.getRecord(),dto);
				loginService.setCurrentTileRecord(dto, new AsyncCallback() {
					public void onFailure(Throwable error) {
						GWT.log("", error);
					}
					@Override
					public void onSuccess(Object result) {
						//noop
					}
				});
				prepareAndShowEditForm(event.getRecord());
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
		hStack.addMember(boundFormVStack);
	}
	
	private void prepareAndShowEditForm(TileRecord tileRecord) {
		boundSwagForm.editRecord(tileRecord);
		currentSwagImage.setSrc("/swag/showImage/" + tileRecord.getAttribute("imageKey"));  
		currentSwagImage.setWidth(283);
		currentSwagImage.setHeight(212);
		
		Long currentKey = Long.valueOf(tileRecord.getAttribute("key"));
		starClickHandler1.setSwagItemKey(currentKey);
		starClickHandler2.setSwagItemKey(currentKey);
		starClickHandler3.setSwagItemKey(currentKey);
		starClickHandler4.setSwagItemKey(currentKey);
		starClickHandler5.setSwagItemKey(currentKey);
		
		updateUserRatingStars(currentKey);
		
		starHStack.show();
		boundFormVStack.show();
	}

	//handle current userRating star colors
	private void updateUserRatingStars(Long currentKey) {
		Integer userRating = (loginInfo.getSwagItemRating(currentKey)!=null)
								?loginInfo.getSwagItemRating(currentKey).getUserRating()
								:0;
		for (int i = 0; i < 5; i++) {
			if (i<userRating) {
				starImages.get(i).setSrc("/images/starOn.gif");
			}
			else {
				starImages.get(i).setSrc("/images/starOff.gif");
			}
		}
	}
	
	public class StarClickHandler implements ClickHandler {
		private SwagItemRating newRating;
		public StarClickHandler(int rating) {
			this.newRating=new SwagItemRating(null,rating); //key is set later
		}
		public void setSwagItemKey(Long swagItemKey) {
			newRating.setSwagItemKey(swagItemKey);
		}
		@Override
		public void onClick(ClickEvent event) {
			if (!loginInfo.isLoggedIn()) {
				Window.open(loginInfo.getLoginUrl(), "_self", ""); 
			}
			else { //logged in
				event.getSource();
				loginService.addOrUpdateRating(loginInfo.getEmail(), 
					newRating,
					new AsyncCallback() {
						public void onFailure(Throwable error) {
							GWT.log("", error);
						}
						@Override
						public void onSuccess(Object result) {
							//refresh client side userRating
							loginInfo.getSwagItemRatings().remove(newRating);
							loginInfo.getSwagItemRatings().add(newRating);
							
							//update stars
							updateUserRatingStars(newRating.getSwagItemKey());
							
							//refresh item
							//kludge to execute a fetch threw saveData()
							boundSwagForm.getField("isFetchOnly").setValue(true);
							boundSwagForm.saveData();
							
						}
				}
				);
			}
		}
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
