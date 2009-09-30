package com.swagswap.service;

import java.util.Date;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.users.UserService;
import com.swagswap.dao.UserDao;
import com.swagswap.domain.SwagImage;
import com.swagswap.domain.SwagItem;
import com.swagswap.domain.SwagSwapUser;

public class SwagSwapUserServiceImpl implements SwagSwapUserService {
	private static final Logger log = Logger.getLogger(SwagSwapUserServiceImpl.class);

	@Autowired
	private UserDao userDao;
	
	/* (non-Javadoc)
	 * @see com.swagswap.service.SwagSwapUserService#get(java.lang.Long)
	 */
	public SwagSwapUser get(Long key) {
		UserService userService;
		return userDao.get(key);
	}
	
	// This is needed because it's called from ItemService.save()
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void insert(SwagSwapUser swagSwapUser) throws UserAlreadyExistsException {
		if (findByEmail(swagSwapUser.getEmail()) !=null) {
			throw new UserAlreadyExistsException(swagSwapUser);
		}
		userDao.insert(swagSwapUser);
	}
	
	public void update(SwagSwapUser swagSwapUser) {
		userDao.update(swagSwapUser);
	}
	
	/* (non-Javadoc)
	 * @see com.swagswap.service.SwagSwapUserService#findByEmail(java.lang.String)
	 */
	public SwagSwapUser findByEmail(String email) {
		return userDao.findByEmail(email);
	}
	
	/* (non-Javadoc)
	 * @see com.swagswap.service.SwagSwapUserService#addUserRating(com.swagswap.domain.User, java.lang.Long, java.lang.Integer)
	 */
	public void addUserRating(SwagSwapUser swagSwapUser, Long swagItemKey, Integer rating){
		throw new NotImplementedException();
	}
		
}
