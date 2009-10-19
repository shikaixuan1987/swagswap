package com.swagswap.web.gwt.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceFloatField;
import com.smartgwt.client.data.fields.DataSourceImageField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.tile.TileRecord;
import com.swagswap.web.gwt.client.domain.SwagItemGWTDTO;

/**
 * This is needed by SmartGWT containers
 * This is inspired by http://code.google.com/p/smartgwt-extensions/source/browse/trunk/src/main/java/com/smartgwt/extensions/gwtrpcds/client/example/SimpleGwtRPCDS.java
 */
public class SimpleGwtRPCDataSource extends GwtRpcDataSource {

	// TODO is singleton what we want here?
	private static SimpleGwtRPCDataSource instance = null;

	public static SimpleGwtRPCDataSource getInstance() {
		if (instance == null) {
			instance = new SimpleGwtRPCDataSource();
		}
		return instance;
	}

	private SimpleGwtRPCDataSource() {
		DataSourceField key = new DataSourceIntegerField("key");
		key.setPrimaryKey(true);
	    // AutoIncrement on server.
		key.setRequired (false);
        addField (key);

        addField(new DataSourceTextField("name", "Name", 20, true));
        addField(new DataSourceTextField("company", "Company", 20, true));
        addField(new DataSourceTextField("description", "Description", 30, true));
		
		DataSourceImageField imageField = new DataSourceImageField("imageKey", "Image");
		imageField.setImageURLPrefix("/swag/showImage/");
		addField(imageField);
		
		//do we want this exposed?
		addField(new DataSourceTextField("ownerEmail", "Owner", 20, true));
		addField(new DataSourceTextField("ownerNickName", "Owner Nick Name", 20, true));
		addField(new DataSourceFloatField("averageRating", "Avg Rating", 5, true));
		addField(new DataSourceIntegerField("numberOfRatings", "No. Ratings", 5, true));
		addField(new DataSourceDateField("created", "Created", 10, true));
		addField(new DataSourceDateField("lastUpdated", "Updated", 10, true));
		
		//TODO add tags and comments
	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		// This can be used as parameter for server side sorting.
		// request.getSortBy ();

		// Finding which rows were requested
		// Normaly these two indexes should be passed to server
		// but for this example I will do "paging" on client side
		// final int startIndex = (request.getStartRow () <
		// 0)?0:request.getStartRow ();
		final int startIndex = 0;
		// final int endIndex = (request.getEndRow () ==
		// null)?-1:request.getEndRow ();
		final int endIndex = -1;
		SimpleGwtRPCDSServiceAsync service = GWT
				.create(SimpleGwtRPCDSService.class);
		service.fetch(new AsyncCallback<List<SwagItemGWTDTO>>() {
			public void onFailure(Throwable caught) {
				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}

			public void onSuccess(List<SwagItemGWTDTO> result) {
				// Calculating size of return list
				int size = result.size();
				// if (endIndex >= 0) {
				// if (endIndex < startIndex) {
				// size = 0;
				// }
				// else {
				// size = endIndex - startIndex + 1;
				// }
				// }
				// Create list for return - it is just requested records
				TileRecord[] list = new TileRecord[size];
				if (size > 0) {
					for (int i = 0; i < result.size(); i++) {
						// no paging
						// if (endIndex >0 && (i >= startIndex && i <=
						// endIndex)) {
						TileRecord record = new TileRecord();
						copyValues(result.get(i), record);
						list[i - startIndex] = record;
						// }
					}
				}
				response.setData(list);
				// IMPORTANT: for paging to work we have to specify size of full
				// result set
				response.setTotalRows(result.size());
				processResponse(requestId, response);
			}
		});
	}

	@Override
	protected void executeAdd(final String requestId, final DSRequest request,
			final DSResponse response) {
		// Retrieve record which should be added.
		JavaScriptObject data = request.getData();
		TileRecord rec = new TileRecord(data);
		SwagItemGWTDTO testRec = new SwagItemGWTDTO();
		copyValues(rec, testRec);
		SimpleGwtRPCDSServiceAsync service = GWT
				.create(SimpleGwtRPCDSService.class);
		service.add(testRec, new AsyncCallback<SwagItemGWTDTO>() {
			public void onFailure(Throwable caught) {
				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}

			public void onSuccess(SwagItemGWTDTO result) {
				TileRecord[] list = new TileRecord[1];
				TileRecord newRec = new TileRecord();
				copyValues(result, newRec);
				list[0] = newRec;
				response.setData(list);
				processResponse(requestId, response);
			}
		});
	}

	@Override
	protected void executeUpdate(final String requestId,
			final DSRequest request, final DSResponse response) {
		// Retrieve record which should be updated.
		// Next line would be nice to replace with line:
		// TileRecord rec = request.getEditedRecord ();
		TileRecord rec = getEditedRecord(request);
		SwagItemGWTDTO testRec = new SwagItemGWTDTO();
		copyValues(rec, testRec);
		SimpleGwtRPCDSServiceAsync service = GWT
				.create(SimpleGwtRPCDSService.class);
		service.update(testRec, new AsyncCallback<SwagItemGWTDTO>() {
			public void onFailure(Throwable caught) {
				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}

			public void onSuccess(SwagItemGWTDTO result) {
				TileRecord[] list = new TileRecord[1];
				TileRecord updRec = new TileRecord();
				copyValues(result, updRec);
				list[0] = updRec;
				response.setData(list);
				processResponse(requestId, response);
			}
		});
	}

	@Override
	protected void executeRemove(final String requestId,
			final DSRequest request, final DSResponse response) {
		// Retrieve record which should be removed.
		JavaScriptObject data = request.getData();
		final TileRecord rec = new TileRecord(data);
		SwagItemGWTDTO testRec = new SwagItemGWTDTO();
		copyValues(rec, testRec);
		SimpleGwtRPCDSServiceAsync service = GWT
				.create(SimpleGwtRPCDSService.class);
		service.remove(testRec, new AsyncCallback<Object>() {
			public void onFailure(Throwable caught) {
				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}

			public void onSuccess(Object result) {
				TileRecord[] list = new TileRecord[1];
				// We do not receive removed record from server.
				// Return record from request.
				list[0] = rec;
				response.setData(list);
				processResponse(requestId, response);
			}

		});
	}

	private static void copyValues(TileRecord from, SwagItemGWTDTO to) {
		to.setKey(Long.valueOf(from.getAttributeAsInt("key")));
		to.setName(from.getAttributeAsString("name"));
		to.setCompany(from.getAttributeAsString("company"));
		to.setDescription(from.getAttributeAsString("description"));
//		to.setSwagImage(from.getAttributeAs("image")); //TODO what do we do about image?
		to.setName(from.getAttributeAsString("name"));
		//TODO email?
		to.setOwnerNickName(from.getAttributeAsString("ownerNickName"));
		//TODO the rest?
	}

	private static void copyValues(SwagItemGWTDTO from, TileRecord to) {
		to.setAttribute("key", from.getKey());
		to.setAttribute("name", from.getName());
		to.setAttribute("company", from.getCompany());
		to.setAttribute("description", from.getDescription());
		to.setAttribute("imageKey", from.getImageKey());
		to.setAttribute("ownerEmail", from.getOwnerEmail());
		to.setAttribute("ownerNickName", from.getOwnerNickName());
		to.setAttribute("averageRating", from.getAverageRating());
		to.setAttribute("numberOfRatings", from.getNumberOfRatings());
		to.setAttribute("created", from.getCreated());
		to.setAttribute("lastUpdated", from.getLastUpdated());
	}

	private TileRecord getEditedRecord(DSRequest request) {
		// Retrieving values before edit
		JavaScriptObject oldValues = request
				.getAttributeAsJavaScriptObject("oldValues");
		// Creating new record for combining old values with changes
		TileRecord newRecord = new TileRecord();
		// Copying properties from old record
		JSOHelper.apply(oldValues, newRecord.getJsObj());
		// Retrieving changed values
		JavaScriptObject data = request.getData();
		// Apply changes
		JSOHelper.apply(data, newRecord.getJsObj());
		return newRecord;
	}
}