package com.swagswap.dao;

import java.util.Collection;
import java.util.List;

import com.swagswap.domain.SwagItem;

public interface SwagItemDao {

	public abstract SwagItem get(Long id);

	@SuppressWarnings("unchecked")
	public abstract List<SwagItem> search(String searchString);

	@SuppressWarnings("unchecked")
	public abstract List<SwagItem> getAll();

	public abstract void save(SwagItem swagItem);

	public abstract void delete(Long id);

}