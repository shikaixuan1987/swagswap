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
	
	public SwagSwapUser findByEmail(String email) {
		return userDao.findByEmail(email);
	}
	
	/* (non-Javadoc)
	 * @see com.swagswap.service.SwagSwapUserService#findByEmail(java.lang.String)
	 * TODO integration test me
	 */
	public SwagSwapUser findByEmailOrCreate(String email) {
		SwagSwapUser swagSwapUser = userDao.findByEmail(email);
		if (swagSwapUser==null) {
			swagSwapUser = new SwagSwapUser(email);
			insert(swagSwapUser);
		}
		return swagSwapUser;
	}
	
	/* (non-Javadoc)
	 * @see com.swagswap.service.SwagSwapUserService#addUserRating(com.swagswap.domain.User, java.lang.Long, java.lang.Integer)
	 */
	//TODO make transactional ?
	public void addOrUpdateRating(String userEmail, SwagItemRating newSwagItemRating){
		SwagSwapUser swagSwapUser = findByEmailOrCreate(userEmail);
		
		//previousRating will be null if this is a new rating
		SwagItemRating previousRating = swagSwapUser.getSwagItemRating(newSwagItemRating.getSwagItemKey());
		recomputeAndRecordTotalRating(previousRating, newSwagItemRating);
		if (previousRating!=null) {
			swagSwapUser.getSwagItemRatings().remove(previousRating);
		}
		swagSwapUser.getSwagItemRatings().add(newSwagItemRating);
		update(swagSwapUser);
	}

	/**
	 * @param previousRating can be null
	 * @param newSwagItemRating
	 * TODO transaction requiresNew?
	 */
	private void recomputeAndRecordTotalRating(SwagItemRating previousRating, SwagItemRating newSwagItemRating) {
		//start with new rating which will be used if this is their first rating of this item
		int computedRatingDifference = newSwagItemRating.getUserRating().intValue(); 
		boolean isNew=true;
		//If they have a previous rating, calculate the difference and update the total item rating with that.
		if (previousRating!=null) {
			if (previousRating.getUserRating().equals(newSwagItemRating.getUserRating())) {
				return; //they submitted the same rating as before
			}
			isNew=false;
			computedRatingDifference=newSwagItemRating.getUserRating().intValue()-previousRating.getUserRating().intValue();
		}
		itemService.updateRating(newSwagItemRating.getSwagItemKey(),computedRatingDifference,isNew);
	}
		
}
