package com.swagswap.web.gwt.client;

import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.swagswap.domain.SwagItem;
import com.swagswap.domain.SwagItemComment;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("itemServiceGWT")
public interface ItemServiceGWTWrapper extends RemoteService {
	SwagItem get(Long id);

	SwagItem get(Long id, boolean loadSwagImage);

	Collection<SwagItem> search(String queryString);

	List<SwagItem> getAll();

	void save(SwagItem swagItem);
	
	void updateRating(Long swagItemKey, int computedRatingDifference, boolean isNew);
	
	void delete(Long id);
	
	void addComment(SwagItemComment swagItemComment);
}
