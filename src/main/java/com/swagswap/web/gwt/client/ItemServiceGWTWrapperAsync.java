package com.swagswap.web.gwt.client;

import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.swagswap.domain.SwagItem;
import com.swagswap.domain.SwagItemComment;

/**
 * The async counterpart of <code>ItemServiceGWTWrapper</code>.
 */
public interface ItemServiceGWTWrapperAsync {
	
	void get(Long id, AsyncCallback<SwagItem> callback);

	void get(Long id, boolean loadSwagImage, AsyncCallback<SwagItem> callback);

	void search(String queryString, AsyncCallback<Collection<SwagItem>> callback);

	void getAll(AsyncCallback<List<SwagItem>> callback);

	void save(SwagItem swagItem, AsyncCallback callback);
	
	void updateRating(Long swagItemKey, int computedRatingDifference, boolean isNew, AsyncCallback callback);
	
	void delete(Long id, AsyncCallback callback);
	
	void addComment(SwagItemComment swagItemComment, AsyncCallback callback);
}
