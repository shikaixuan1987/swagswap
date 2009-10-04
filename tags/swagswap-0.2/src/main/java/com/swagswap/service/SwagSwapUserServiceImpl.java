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
	protected SwagSwapUserServiceImpl(UserDao userDao, ItemService itemService) {
		this.userDao = userDao;
		this.itemService = itemService;
	}

	/* (non-Javadoc)
	 * @see com.swagswap.service.SwagSwapUserService#get(java.lang.Long)
	 */
	public SwagSwapUser get(Long key) {
		UserService userService;
		return userDao.get(key);
	}
	
	//if you make this transactional the user doesn't seem to be retrieved with ratings :(
//	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
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
	
/*	 I wanted to include this method so that the front end wouldn't have to deal with 
	 just in time creation of users in our db if they don't exist. The reason this was needed is that since
	 we're using google authentication, we never know if a user is logged in with google 
	 (and our app) but doesn't have a user in our DB. Only problem is that since SwagSwapUser
	 is not in the same entity group as anything else, you can't insert a user in the same
	 transaction as, say a SwagItem or a SwagItemRating.  In the end I had to let the front end
	 do it in two calls :(
	 
	 I also tried making this method require a new transaction. When i did that the added user
	 didn't seem to be committed in time. Maybe it's just too late.
	

	
 */
	//If you don't make this transactional, the call from SwagSwapUserService.addOrUpdateRating()
	//fails since you can't operate on 2 different entity groups in one transaction
	//however if you do make it transactional it doesn't seem to update the user rating properly
//	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public SwagSwapUser findByEmailOrCreate(String email) {
		SwagSwapUser swagSwapUser = findByEmail(email);
		if (swagSwapUser==null) {
			swagSwapUser = new SwagSwapUser(email); //TODO do we need to add nickname?
			insert(swagSwapUser);
		}
		return swagSwapUser;
	}
	
	/* (non-Javadoc)
	 * @see com.swagswap.service.SwagSwapUserService#addUserRating(com.swagswap.domain.User, java.lang.Long, java.lang.Integer)
	 */
	public void addOrUpdateRating(String email, SwagItemRating newSwagItemRating){
		//previousRating will be null if this is a new rating

		SwagSwapUser swagSwapUser = findByEmailOrCreate(email);
		swagSwapUser.getSwagItemRatings();
		SwagItemRating previousRating = swagSwapUser.getSwagItemRating(newSwagItemRating.getSwagItemKey());
		//get the data we need before deleting it
		Integer previousRatingValue = (previousRating==null)?null:previousRating.getUserRating(); 
		//add or update user's rating of this swagItem
		if (previousRating!=null) {
			swagSwapUser.getSwagItemRatings().remove(previousRating);
		}
		swagSwapUser.getSwagItemRatings().add(newSwagItemRating);
		
		//update swagItem with new average rating
		recomputeAndRecordSwagItemAverageRating(previousRatingValue, newSwagItemRating);
	}

	/**
	 * @param previousRating can be null
	 * @param newSwagItemRating
	 * TODO transaction requiresNew?
	 */
	private void recomputeAndRecordSwagItemAverageRating(Integer previousRatingValue, SwagItemRating newSwagItemRating) {
		//start with new rating which will be used if this is their first rating of this item
		int computedRatingDifference = newSwagItemRating.getUserRating().intValue(); 
		boolean isNew=true;
		//If they have a previous rating, calculate the difference and update the total item rating with that.
		if (previousRatingValue!=null) {
			if (previousRatingValue.equals(newSwagItemRating.getUserRating())) {
				return; //they submitted the same rating as before
			}
			isNew=false;
			computedRatingDifference=newSwagItemRating.getUserRating().intValue()-previousRatingValue.intValue();
		}
		itemService.updateRating(newSwagItemRating.getSwagItemKey(),computedRatingDifference,isNew);
	}
		
}