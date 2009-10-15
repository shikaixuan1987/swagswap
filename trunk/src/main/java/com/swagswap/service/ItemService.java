package com.swagswap.service;

import java.util.Collection;
import java.util.List;

import com.swagswap.domain.SwagItem;
import com.swagswap.domain.SwagItemComment;
import com.swagswap.exceptions.LoadImageFromURLException;

public interface ItemService {

	SwagItem get(Long id);

	SwagItem get(Long id, boolean loadSwagImage);

	List<SwagItem> search(String queryString);

	List<SwagItem> getAll();

	void save(SwagItem swagItem) throws LoadImageFromURLException;
	
	void updateRating(Long swagItemKey, int computedRatingDifference, boolean isNew);
	
	void delete(Long id);
	
	void addComment(SwagItemComment swagItemComment);

}
