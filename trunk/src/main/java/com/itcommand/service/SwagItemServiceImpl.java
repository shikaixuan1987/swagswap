package com.itcommand.service;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.itcommand.dao.SwagItemDao;
import com.itcommand.domain.SwagItem;

public class SwagItemServiceImpl implements SwagItemService {

	private static final Logger log = Logger.getLogger(SwagItemServiceImpl.class);

	@Autowired
	private SwagItemDao swagItemDao;

	public SwagItem get(Long id) {
		return swagItemDao.get(id);
	}

	public Collection<SwagItem> search(String queryString) {
		return swagItemDao.search(queryString);
	}

	public List<SwagItem> getAll() {
		return swagItemDao.getAll();
	}

	public void save(SwagItem swagItem) {
		swagItemDao.save(swagItem);
	}

	public void delete(Long id) {
		swagItemDao.delete(id);
	}

	public void setSwagItemDao(SwagItemDao swagItemDao) {
		this.swagItemDao = swagItemDao;
	}
	
}