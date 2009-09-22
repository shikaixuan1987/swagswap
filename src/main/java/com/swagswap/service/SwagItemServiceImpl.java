package com.swagswap.service;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.swagswap.dao.SwagItemDao;
import com.swagswap.domain.SwagItem;

public class SwagItemServiceImpl implements SwagItemService {

	private static final Logger log = Logger.getLogger(SwagItemServiceImpl.class);

	@Autowired
	private SwagItemDao swagItemDao;

	/**
	 * Load swagItem, but not associated swagImage
	 */
	public SwagItem get(Long id) {
		return get(id, false);
	}
	
	/**
	 * 
	 * @param id
	 * @param loadSwagImage whether to load swagImage (it is lazy loaded by JDO)
	 * @return SwagItem if found
	 * @throws Exception if item not found
	 */
	public SwagItem get(Long id, boolean loadSwagImage) {
		return swagItemDao.get(id, loadSwagImage);
	}

	public Collection<SwagItem> search(String queryString) {
		return swagItemDao.search(queryString);
	}

	public List<SwagItem> getAll() {
		return swagItemDao.getAll();
	}


	/**
	 * @return key of saved item
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void save(SwagItem swagItem) {
		swagItemDao.save(swagItem);
		//to test transactions, uncomment the throw exception line below
		//and try this method with and without the annotation
		//throw new RuntimeException("see if it rolls back");
	}

	public void delete(Long id) {
		swagItemDao.delete(id);
	}

	public void setSwagItemDao(SwagItemDao swagItemDao) {
		this.swagItemDao = swagItemDao;
	}
	
}