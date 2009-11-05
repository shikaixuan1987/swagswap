package com.swagswap.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.swagswap.dao.UserDao;
import com.swagswap.domain.SwagItem;
import com.swagswap.domain.SwagItemRating;
import com.swagswap.domain.SwagSwapUser;
import com.swagswap.exceptions.AccessDeniedException;
import com.swagswap.exceptions.InvalidSwagItemRatingException;
import com.swagswap.exceptions.UserAlreadyExistsException;

/**
 * Maintains users in our DB which are used for authorization and holding SwagItem ratings.
 * Also wraps Google's user service and uses it's authentication functionality
 *
 */
public class SwagSwapUserServiceImpl implements SwagSwapUserService {
	private static final Logger log = Logger.getLogger(SwagSwapUserServiceImpl.class);

	@Autowired
	private UserDao userDao;
	@Autowired
	private ItemService itemService;
	@Autowired
	private MailService mailService;
	//created with a factory in the config
	private UserService googleUserService;
	
	
	public SwagSwapUserServiceImpl() {
	}

	//for Integration test
	protected SwagSwapUserServiceImpl(UserDao userDao, ItemService itemService, UserService googleUserService) {
		this.userDao = userDao;
		this.itemService = itemService;
		this.googleUserService=googleUserService;
	}
	
	public List<SwagSwapUser> getAll() {
		return userDao.getAll();
	}

	public SwagSwapUser get(Long key) {
		return userDao.get(key);
	}
	
	//if you make this transactional the user doesn't seem to be retrieved with ratings :(
//	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void insert(SwagSwapUser swagSwapUser) throws UserAlreadyExistsException {
		if (findByEmail() !=null) {
			throw new UserAlreadyExistsException(swagSwapUser);
		}
		userDao.insert(swagSwapUser);
		//Welcome message
		mailService.send(swagSwapUser.getGoogleID(), swagSwapUser.getEmail(),
				"Welcome to SwagSwap!",
				"To email a swagitem to be shown live on our site do the following:" +
				"\n<br/><br/>Compose an email to <a href=\"mailto:add@swagswap.appspotmail.com?subject="+
				swagSwapUser.getKey()+ "\">add@swagswap.appspotmail.com</a>" +
				"\n<br/>Put your secret code in the subject: " + swagSwapUser.getKey() +
				"\n<br/>Put the name of the swag item in the mail body" +
				"\n<br/>Optionally attatch an image to your mail" +
				"\n\n<br/><br/>Regards,\n<br/>The SwagSwap Team"
		);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.SUPPORTS)
	public void update(SwagSwapUser swagSwapUser) {
		userDao.update(swagSwapUser);
	}
	
	public SwagSwapUser findByGoogleID(String googleID) {
		return userDao.findByGoogleID(googleID);
	}
	
	public SwagSwapUser findByEmail() {
		if (getCurrentUser()==null) {
			return null;
		}
		String email = getCurrentUser().getEmail();
		if (isBlackListed(email)) {
			throw new AccessDeniedException("User " + email + " is blacklisted");
		}
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
	//TODO this can take 0 params
	public SwagSwapUser findByEmailOrCreate() {
		SwagSwapUser swagSwapUser = findByEmail();
		if (swagSwapUser==null) {
			swagSwapUser = new SwagSwapUser(getCurrentUser().getEmail(),getCurrentUser().getUserId(), getCurrentUser().getNickname());
			insert(swagSwapUser);
		}
		return swagSwapUser;
	}
	
	//TODO take out email param here. unused
	public void addOrUpdateRating(String email, SwagItemRating newSwagItemRating) throws InvalidSwagItemRatingException {
		if (newSwagItemRating.getSwagItemKey() == null || newSwagItemRating.getUserRating() == null) {
			throw new InvalidSwagItemRatingException(newSwagItemRating);
		}
		if (StringUtils.isEmpty(email)) {
			throw new AccessDeniedException("Can't rate an item when not logged in");
		}
		SwagSwapUser swagSwapUser = findByEmailOrCreate();
		swagSwapUser.getSwagItemRatings();
		//previousRating will be null if this is a new rating
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
	
	public void blackListUser(String email) {
		userDao.blackListUser(email);
	}
	
	public boolean isBlackListed(String email) {
		return userDao.isBlackListed(email);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.SUPPORTS)
	public void optOut(String googleID, boolean optOut) {
		userDao.optOut(googleID, optOut);
	}
	
	/**
	 * Wrapped Google UserService methods so that we can 
	 * add openId or something else later
	 */
	
	public String createLoginURL(String destinationURL) {
		return googleUserService.createLoginURL(destinationURL);
	}
	
	public String createLogoutURL(String destinationURL) {
		return googleUserService.createLogoutURL(destinationURL);
	}
	
	/**
	 * Expects users to be logged in, otherwise throws a AccessDeniedException
	 */
	public User getCurrentUser() {
		User user = googleUserService.getCurrentUser();
		if (user==null) {
			throw new AccessDeniedException("User not logged in.  No permissions");
		}
		return user;
	}
	
	public boolean isUserAdmin() {
		//  If use is not logged in then clearly hes not an admin
		if (!isUserLoggedIn()) {
			return false;
		}
		return googleUserService.isUserAdmin();
	}
	
	public boolean isUserLoggedIn() {
		return googleUserService.isUserLoggedIn();
	}

	/**
	 * End Wrapped Google UserService methods
	 */

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

	// for DI
	public void setGoogleUserService(UserService googleUserService) {
		this.googleUserService = googleUserService;
	}

	public boolean isItemOwner(SwagItem item) {
		if (!isUserLoggedIn()) {
			return false;
		}
		return (getCurrentUser().getNickname().equals(item.getOwnerNickName()));
	}

}
