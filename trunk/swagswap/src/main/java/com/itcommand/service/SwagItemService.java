package com.itcommand.service;

import java.util.Collection;
import java.util.List;

import com.itcommand.domain.SwagItem;

public interface SwagItemService {

	public abstract SwagItem get(Long id);

	@SuppressWarnings("unchecked")
	public abstract Collection<SwagItem> search(String queryString);

	@SuppressWarnings("unchecked")
	public abstract List<SwagItem> getAll();

	public abstract void save(SwagItem swagItem);

	public abstract void delete(Long id);

}