package com.swagswap.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.appengine.api.users.UserService;
import com.swagswap.dao.UserDao;
import com.swagswap.domain.SwagItemRating;
import com.swagswap.domain.SwagSwapUser;

public class SwagSwapUserServiceImpl implements SwagSwapUserService {
	private static final Logger log = Logger.getLogger(SwagSwapUserServiceImpl.class);

	@Autowired
	private UserDao userDao;
	@Autowired
	private ItemService itemService;
	
	
	public SwagSwapUserServiceImpl() {
	}

	//for Integration test
	protected SwagSwapUserServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}

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
	//TODO make transactional ?
	public void addOrUpdateRating(String userEmail, SwagItemRating newSwagItemRating){
		SwagSwapUser swagSwapUser = findByEmail(userEmail);
		
		SwagItemRating previousRating = swagSwapUser.getSwagItemRating(newSwagItemRating.getSwagItemKey());
		recomputeSwagItemRating(previousRating, newSwagItemRating);
		if (previousRating!=null) {
			swagSwapUser.getSwagItemRatings().remove(previousRating);
		}
		swagSwapUser.getSwagItemRatings().add(newSwagItemRating);
		
		update(swagSwapUser);
	}

	//TODO implement me
	/**
	 * @param previousRating can be null
	 * @param newSwagItemRating
	 */
	private void recomputeSwagItemRating(SwagItemRating previousRating, SwagItemRating newSwagItemRating) {
		if (previousRating!=null) {
			if (previousRating.getUserRating().equals(newSwagItemRating.getUserRating())) {
				return; //they submitted the same rating as before
			}
			//TODO update average accordingly by taking out their old rating and adding their new one
			newSwagItemRating.setUserRating(newSwagItemRating.getUserRating()-previousRating.getUserRating());
			itemService.updateRating(newSwagItemRating);
		}
		else { //new rating
			itemService.updateRating(newSwagItemRating);
		}
	}
		
}
