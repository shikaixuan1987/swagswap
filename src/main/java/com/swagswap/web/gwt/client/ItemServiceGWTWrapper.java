package com.swagswap.web.gwt.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.swagswap.domain.SwagItemComment;
import com.swagswap.web.gwt.client.domain.SwagItemGWTDTO;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("itemServiceGWT")
public interface ItemServiceGWTWrapper extends RemoteService {
	SwagItemGWTDTO get(Long id);

	SwagItemGWTDTO get(Long id, boolean loadSwagImage);

	ArrayList<SwagItemGWTDTO> search(String queryString);

	ArrayList<SwagItemGWTDTO> getAll();

	void save(SwagItemGWTDTO swagItemDto);
	
	void updateRating(Long swagItemKey, int computedRatingDifference, boolean isNew);
	
	void delete(Long id);
	
	void addComment(SwagItemComment swagItemComment);
}
