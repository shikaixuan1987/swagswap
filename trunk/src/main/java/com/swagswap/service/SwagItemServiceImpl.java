package com.swagswap.service;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.appengine.api.users.User;
import com.swagswap.dao.SwagItemDao;
import com.swagswap.domain.SwagItem;

/**
 * For transactionality and will be used for caching.
 * @author sam
 *
 */
public class SwagItemServiceImpl implements SwagItemService {

	private static final Logger log = Logger.getLogger(SwagItemServiceImpl.class);

	@Autowired
	private SwagItemDao swagItemDao;
	@Autowired
	private com.google.appengine.api.users.UserService googleUserService;

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

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void save(SwagItem swagItem) {
		if (swagItem.isNew()) {
			swagItem.setOwnerEmail(googleUserService.getCurrentUser().getEmail());
			swagItem.setOwnerNickName(googleUserService.getCurrentUser().getNickname());
			swagItemDao.insert(swagItem);
		}
		else { //update
			checkPermissions(swagItem.getKey());
			swagItemDao.update(swagItem);
		}
		//to test transactions, uncomment the throw exception line below
		//and try this method with and without the annotation
		//throw new RuntimeException("see if it rolls back");
	}

	public void delete(Long id) {
		checkPermissions(id);
		swagItemDao.delete(id);
	}

	public void setSwagItemDao(SwagItemDao swagItemDao) {
		this.swagItemDao = swagItemDao;
	}

	public void setGoogleUserService(com.google.appengine.api.users.UserService googleUserService) {
		this.googleUserService = googleUserService;
	}
	
	/**
	 * @param id
	 * @throws AccessDeniedException
	 */
	private void checkPermissions(Long id) throws AccessDeniedException {
		User user = googleUserService.getCurrentUser();
		SwagItem swagItemToCheck = get(id); //get it fresh to prevent client side attacks
		if (
				(user==null) ||
				(!user.getEmail().equals(swagItemToCheck.getOwnerEmail()))
			) {
			throw new AccessDeniedException(
					swagItemToCheck,
					user.getEmail());
		}
	}
}