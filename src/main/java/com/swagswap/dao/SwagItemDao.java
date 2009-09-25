package com.swagswap.dao;

import java.util.List;

import com.swagswap.domain.SwagItem;

public interface SwagItemDao {

	SwagItem get(Long id);

	SwagItem get(Long id, boolean loadSwagImage);
	
	List<SwagItem> search(String searchString);

	List<SwagItem> getAll();

	void save(SwagItem swagItem);

	void delete(Long id);

}