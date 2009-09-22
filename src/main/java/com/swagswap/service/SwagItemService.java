package com.swagswap.service;

import java.util.Collection;
import java.util.List;

import com.swagswap.domain.SwagItem;

public interface SwagItemService {

	public abstract SwagItem get(Long id);
	
	public SwagItem get(Long id, boolean loadSwagImage);
	
	public abstract Collection<SwagItem> search(String queryString);

	public abstract List<SwagItem> getAll();

	public abstract void save(SwagItem swagItem);

	public abstract void delete(Long id);
}
