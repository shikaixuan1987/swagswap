package com.swagswap.service;

import java.util.Collection;
import java.util.List;

import com.swagswap.domain.SwagItem;
import com.swagswap.domain.SwagItemRating;

public interface ItemService {

	SwagItem get(Long id);

	SwagItem get(Long id, boolean loadSwagImage);

	Collection<SwagItem> search(String queryString);

	List<SwagItem> getAll();

	void save(SwagItem swagItem);

	void delete(Long id);

	void updateRating(SwagItemRating newSwagItemRating);
}
