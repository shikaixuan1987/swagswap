package com.swagswap.web.gwt.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.swagswap.domain.SwagItemComment;
import com.swagswap.web.gwt.client.domain.SwagItemGWTDTO;

/**
 * The async counterpart of <code>ItemServiceGWTWrapper</code>.
 */
public interface ItemServiceGWTWrapperAsync {
	
	void get(Long id, AsyncCallback<SwagItemGWTDTO> callback);

	void get(Long id, boolean loadSwagImage, AsyncCallback<SwagItemGWTDTO> callback);

	void search(String queryString, AsyncCallback<ArrayList<SwagItemGWTDTO>> callback);

	void getAll(AsyncCallback<ArrayList<SwagItemGWTDTO>> callback);

	void save(SwagItemGWTDTO swagItem, AsyncCallback callback);
	
	void updateRating(Long swagItemKey, int computedRatingDifference, boolean isNew, AsyncCallback callback);
	
	void delete(Long id, AsyncCallback callback);
	
	void addComment(SwagItemComment swagItemComment, AsyncCallback callback);
}
