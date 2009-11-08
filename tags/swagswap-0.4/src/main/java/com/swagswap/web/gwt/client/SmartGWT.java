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
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.ImageStyle;
import com.smartgwt.client.types.OperatorId;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.form.events.SubmitValuesEvent;
import com.smartgwt.client.widgets.form.events.SubmitValuesHandler;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.SubmitItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VLayout;
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

	//services
	private final SmartGWTItemServiceWrapperAsync itemService = GWT
		.create(SmartGWTItemServiceWrapper.class);
	private LoginServiceAsync loginService = GWT.create(LoginService.class);
	
	//client state
	private LoginInfo loginInfo; //null if they're not logged in

	//global GUI objects TODO: can scope be reduced?
	final TileGrid tileGrid = new TileGrid();
	private VStack boundFormVStack;
	private DynamicForm boundSwagForm;
	private Img currentSwagImage;
	private List<Img> starImages = new ArrayList<Img>();
	private StarClickHandler starClickHandler1;
	private StarClickHandler starClickHandler2;
	private StarClickHandler starClickHandler3;
	private StarClickHandler starClickHandler4;
	private StarClickHandler starClickHandler5;
	private HStack starHStack;
	private DynamicForm uploadForm;
	private HLayout editButtonsLayout;
	
	/**
	 * Check login status and build GUI
	 */
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
				}
			});
	}
	
	public void buildGUI() {
		final VStack mainStack = new VStack(20);
		mainStack.setShowEdges(true);
		mainStack.setWidth100();
		HStack menuHStack= new HStack();
		menuHStack.setHeight(25);
		addSearchPanel(menuHStack);
		addSortPanel(menuHStack);
		addLoginLogoutPanel(menuHStack);
		mainStack.addMember(menuHStack);
		
		HStack itemsAndEditHStack = new HStack();
		addItemsPanel(itemsAndEditHStack);
//		addImageUpload(itemsAndEditHStack);
		addEditForm(itemsAndEditHStack);
		mainStack.addMember(itemsAndEditHStack);

		RootPanel.get("gwt-tilegrid").add(mainStack); //anchored on GWT html page
		mainStack.draw();
	}

	private void addSortPanel(final HStack hStack) {
		final DynamicForm sortForm = new DynamicForm();
		sortForm.setAutoFocus(false);
		sortForm.setNumCols(4);
		sortForm.setWrapItemTitles(false);

		SelectItem sortItem = new SelectItem("sortBy", "Sort By");

		LinkedHashMap valueMap = new LinkedHashMap();
		valueMap.put("name", "Name");
		valueMap.put("company", "Company");
		valueMap.put("averageRating", "Avg Rating");
		valueMap.put("ownerNickName", "Owner");
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
	}

	private void addSearchPanel(final HStack hStack) {
		final DynamicForm filterForm = new DynamicForm();
		filterForm.setNumCols(2);
		filterForm.setDataSource(SmartGWTRPCDataSource.getInstance());
		filterForm.setAutoFocus(false);
		filterForm.setOperator(OperatorId.OR);

		TextItem nameItem = new TextItem("name", "Search");
		nameItem.setOperator(OperatorId.ICONTAINS); // case insensitive
		final HiddenItem companyItem = new HiddenItem("company");
		companyItem.setOperator(OperatorId.ICONTAINS);
		final HiddenItem ownerItem = new HiddenItem("ownerNickName");
		ownerItem.setOperator(OperatorId.ICONTAINS);

		filterForm.setFields(nameItem,companyItem,ownerItem);
		
		filterForm.addItemChangedHandler(new ItemChangedHandler() {
			public void onItemChanged(ItemChangedEvent event) {
				String searchTerm = filterForm.getValueAsString("name");
				companyItem.setValue(searchTerm);
				ownerItem.setValue(searchTerm);
				if (searchTerm==null) {
					tileGrid.fetchData();
				}
				else {
					Criteria criteria = filterForm.getValuesAsCriteria();
					tileGrid.fetchData(criteria);
				}
			}
		});

		hStack.addMember(filterForm);
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
		DetailViewerField company = new DetailViewerField("company");
		DetailViewerField ownerNickName = new DetailViewerField("ownerNickName");
		DetailViewerField averageRating = new DetailViewerField("averageRating");
		//show stars in tile
		averageRating.setDetailFormatter(new DetailFormatter() {
			public String format(Object value, DetailViewerRecord record,
					DetailViewerField field) {
					int averageRating = record.getAttributeAsInt("averageRating");
					int numberOfRatings = record.getAttributeAsInt("numberOfRatings");
				return createStarsHTMLString(averageRating) + " / " + numberOfRatings;
			}
		});
		DetailViewerField lastUpdated = new DetailViewerField("lastUpdated");
		//TODO format date
//		lastUpdated.setDetailFormatter(new DetailFormatter() {
//			public String format(Object value, DetailViewerRecord record,
//					DetailViewerField field) {
//					Date lastUpdated = record.getAttributeAsDate("lastUpdated");
//					SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy HH:MM");
//				return formatter.format(lastUpdated);
//			}
//		});
		tileGrid.setFields(pictureField, name, company ,ownerNickName, averageRating, lastUpdated);
		hStack.addMember(tileGrid);
	}
	
/*	public void addImageUploadForm(HStack hStack) {
//		Img img = new Img("file://c:/photos/june2009/Image008.jpg");
		Image img = new Image("file://c:/photos/june2009/Image008.jpg");
		URL imageUrl = new 
//        hStack.addMember(img);
	}*/
	
	public void addImageUpload(HStack hStack) {
		//create a hidden frame
		Canvas iframe = new Canvas();
		iframe.setID("fileUploadFrame");
		iframe.setContents("<IFRAME NAME=\"fileUploadFrame\" style=\"width:0;height:0;border:0\"></IFRAME>");
		iframe.setVisibility(Visibility.VISIBLE); //Could not get the IFRAME in the page with Visibility HIDDEN
		uploadForm = new DynamicForm();
		uploadForm.setNumCols(4);
		uploadForm.setTarget("fileUploadFrame"); //set target of FORM to the IFRAME
		uploadForm.setEncoding(Encoding.MULTIPART);
		uploadForm.setAction(GWT.getModuleBaseURL()+"TODO-fileuploadservlet");

		//creates a HTML formitem with a textfield and a browse button
		final UploadItem uploadItem = new UploadItem("filename","Select a file");
		uploadItem.setWidth(300);
		uploadItem.addChangedHandler(new ChangedHandler() {
			
			public void onChanged(ChangedEvent event) {
				//The item shows the whole (long) path, so set caret at end 
				//so user can verify his chosen filename
				String filename = (String)uploadItem.getValue();
				if (filename != null) uploadItem.setSelectionRange(filename.length(), filename.length());
				
				//Unfortunately UploadItem does not throw a ChangedEvent :(
			}
		});

		SubmitItem submitItem = new SubmitItem("upload", "Upload");
		submitItem.setStartRow(false);
		submitItem.setEndRow(false);

		uploadForm.setItems(uploadItem, submitItem);

		uploadForm.addSubmitValuesHandler(new SubmitValuesHandler() {
			
			public void onSubmitValues(SubmitValuesEvent event) {
				//maybe do some filename verification
				//String filename = (String)uploadItem.getValue();
				uploadForm.submitForm();
			}
		});

		hStack.addMember(uploadForm);
		hStack.addMember(iframe);
	}
	
/*	public void addImageUploadForm(HStack hStack) {
		FormPanel imageUploadForm = new FormPanel();
		imageUploadForm.setAction("/imageUploadServlet"); // onze image fileupload servlet in het server project
		
		// Because we're going to add a FileUpload widget, we'll need to set the
		// form to use the POST method, and multipart MIME encoding.
		imageUploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		imageUploadForm.setMethod(FormPanel.METHOD_POST);
		
		final FileUpload afbeeldingImageValue = new FileUpload();
		afbeeldingImageValue.setName("uploadFormElement");
		
		imageUploadForm.setWidget(afbeeldingImageValue);
		
		imageUploadForm.addFormHandler(new FormHandler() {
			public void onSubmit(FormSubmitEvent event) {
				if (afbeeldingImageValue.getFilename().contains(" ")) {
					Window.alert("Er mag geen spaties zijn in de naam.");
					event.setCancelled(true);
				}
			}
			public void onSubmitComplete(FormSubmitCompleteEvent event) {
				// TODO: Maybe we can try to refresh here, so that the picture is available right away, instead of after a second visit.
			}
		});
		hStack.addMember(imageUploadForm);
	}
*/	
	public String createStarsHTMLString(float rating) {
		int roundedRating = Math.round(rating);
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			s.append((i<roundedRating)?"<img src=\"images/starOn.gif\"/>":"<img src=\"images/starOff.gif\"/>");
		}
		return s.toString();
	}
	
	private void addLoginLogoutPanel(final HStack hStack) {
		HStack loginPanel = new HStack();
		loginPanel.setWidth100();
//		loginPanel.setHeight(10);
		HStack logoutPanel = new HStack();
		logoutPanel.setWidth100();
//		logoutPanel.setHeight(10);
		Anchor signInLink = new Anchor("Sign In");
		Anchor signOutLink = new Anchor("Sign Out");
		Label addLink = new Label("Add Swag");
		addLink.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				currentSwagImage.setSrc("/images/no_photo.jpg");
				boundFormVStack.hideMember(starHStack);
				boundFormVStack.show();
				boundSwagForm.editNewRecord();
			}
		});

		if (loginInfo.isLoggedIn()) {
			logoutPanel.addMember(addLink);
			signOutLink.setHref(loginInfo.getLogoutUrl());
			logoutPanel.addMember(signOutLink);
			Label welcomeLabel = new Label("Welcome: "
					+ loginInfo.getNickName());
			welcomeLabel.setWrap(false);
			logoutPanel.addMember(welcomeLabel);
			hStack.addMember(logoutPanel);
		} else { //not logged in
			signInLink.setHref(loginInfo.getLoginUrl());
			loginPanel.addMember(signInLink);
			hStack.addMember(loginPanel);
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

		addStarRatings();
		
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
		
		TextItem newImageURLItem = new TextItem("newImageURL");

		boundSwagForm.setFields(nameItem, companyItem, descriptionItem, tag1Item,
				tag2Item, tag3Item, tag4Item, isFetchOnlyItem, imageKeyItem, newImageURLItem);
		boundFormVStack.addMember(boundSwagForm);
		
		currentSwagImage = new Img("/images/no_photo.jpg");  
		currentSwagImage.setImageType(ImageStyle.NORMAL); 
		boundFormVStack.addMember(currentSwagImage);
		
		tileGrid.addRecordClickHandler(new RecordClickHandler() {

			public void onRecordClick(RecordClickEvent event) {
				SwagItemGWTDTO dto = new SwagItemGWTDTO();
				SmartGWTRPCDataSource.copyValues(event.getRecord(),dto);
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
				//TODO
				//uploadForm.submitForm();
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
		
		IButton deleteButton = new IButton("Delete");
		deleteButton.setAutoFit(true);
		deleteButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				showConfirmRemovePopup(tileGrid.getSelectedRecord());
				boundFormVStack.hide();
			}
		});
		editButtonsLayout = new HLayout();
		editButtonsLayout.addMember(saveButton);
		editButtonsLayout.addMember(cancelButton);
		editButtonsLayout.addMember(deleteButton);
		boundFormVStack.addMember(editButtonsLayout);
		boundFormVStack.hide();
		hStack.addMember(boundFormVStack);
	}
	
    private void showConfirmRemovePopup(final TileRecord selectedTileRecord) {
    	final com.smartgwt.client.widgets.Window winModal = new com.smartgwt.client.widgets.Window();
		winModal.setWidth(360);
		winModal.setHeight(115);
		winModal.setTitle("Confirm Delete");
		winModal.setShowMinimizeButton(false);
		winModal.setIsModal(true);
		winModal.setShowModalMask(true);
		winModal.centerInPage();
		winModal.addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				winModal.destroy();
			}
		});
    	
		VLayout vLayout = new VLayout();
		String recordName = selectedTileRecord.getAttributeAsString("name");
		Label confirmText = new Label("Are you sure you want to delete " + recordName + "?");
		confirmText.setHeight(50);
		vLayout.addMember(confirmText);
		
		IButton yesButton = new IButton("Yes");
		yesButton.setAutoFit(true);
		yesButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				tileGrid.removeData(selectedTileRecord);
				winModal.hide();
			}
		});
		IButton cancelButton = new IButton("Cancel");
		cancelButton.setAutoFit(true);
		cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				winModal.hide();
			}
		});
		HLayout buttonsLayout = new HLayout();
		buttonsLayout.addMember(yesButton);
		buttonsLayout.addMember(cancelButton);
		
		vLayout.addMember(buttonsLayout);
		
		winModal.addItem(vLayout);
		winModal.show();
    }
	
	private void addStarRatings() {
		//add five stars
		for (int i = 0; i < 5; i++) {
			starImages.add(new Img("/images/starOff.gif",12,12));
		}
		
		starClickHandler1 = new StarClickHandler();
		starClickHandler2 = new StarClickHandler();
		starClickHandler3 = new StarClickHandler();
		starClickHandler4 = new StarClickHandler();
		starClickHandler5 = new StarClickHandler();
		
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
	}
	
	public class StarClickHandler implements ClickHandler {
		private SwagItemRating newRating;
		public void setSwagItemRating(SwagItemRating swagItemRating) {
			newRating = swagItemRating;
		}
		@Override
		public void onClick(ClickEvent event) {
			//send them to login page if not logged in
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
							SwagItemRating previousRating = loginInfo.getSwagItemRating(newRating.getSwagItemKey());
							if (previousRating!=null) {
								loginInfo.getSwagItemRatings().remove(previousRating);
							}
							loginInfo.getSwagItemRatings().add(newRating);
							
							//update stars
							updateUserRatingStars(newRating.getSwagItemKey());
							
							//refresh item
							//kludge to execute a fetch through saveData()
							boundSwagForm.getField("isFetchOnly").setValue(true);
							boundSwagForm.saveData(new DSCallback() {
								//reselect selected tile (call to saveData deselects it)
								public void execute(DSResponse response,
										Object rawData, DSRequest request) {
									//get updated record
									final TileRecord rec = new TileRecord(request.getData());
									//Note: selectRecord seems to only work on the tile index
									tileGrid.selectRecord(tileGrid.getRecordIndex(rec)); 
								}});
						}
				}
				);
			}
		}
	}
	
	private void prepareAndShowEditForm(TileRecord tileRecord) {
		// make read only if they don't have permission
		String currentSwagItemOwner = tileRecord.getAttribute("ownerID");
		String currentUserId = loginInfo.getID();
		if (loginInfo.isUserAdmin() || currentSwagItemOwner.equals(currentUserId)) {
			boundSwagForm.enable();
			editButtonsLayout.show();
		}
		else {
			boundSwagForm.disable();
			editButtonsLayout.hide();
		}
		boundSwagForm.editRecord(tileRecord);
		currentSwagImage.setSrc("/swag/showImage/" + tileRecord.getAttribute("imageKey"));  
		currentSwagImage.setWidth(283);
		currentSwagImage.setHeight(212);
		
		Long currentKey = Long.valueOf(tileRecord.getAttribute("key"));
		starClickHandler1.setSwagItemRating(new SwagItemRating(currentKey,1));
		starClickHandler2.setSwagItemRating(new SwagItemRating(currentKey,2));
		starClickHandler3.setSwagItemRating(new SwagItemRating(currentKey,3));
		starClickHandler4.setSwagItemRating(new SwagItemRating(currentKey,4));
		starClickHandler5.setSwagItemRating(new SwagItemRating(currentKey,5));
		
		updateUserRatingStars(currentKey);
		
		starHStack.show();
		boundFormVStack.show();
	}

	/**
	 * handle current userRating star colors
	 * @param currentKey
	 */
	private void updateUserRatingStars(Long currentKey) {
		SwagItemRating swagItemRatingForKey = loginInfo.getSwagItemRating(currentKey);
		Integer userRating = (swagItemRatingForKey==null) ? 0 : swagItemRatingForKey.getUserRating();
		
		for (int i = 0; i < 5; i++) {
			if (i<userRating) {
				starImages.get(i).setSrc("/images/starOn.gif");
			}
			else {
				starImages.get(i).setSrc("/images/starOff.gif");
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