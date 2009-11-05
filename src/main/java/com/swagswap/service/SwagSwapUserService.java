package com.swagswap.service;

import com.google.appengine.api.users.User;
import com.swagswap.domain.SwagItem;
import com.swagswap.domain.SwagItemRating;
import com.swagswap.domain.SwagSwapUser;
import com.swagswap.exceptions.InvalidSwagItemRatingException;
import com.swagswap.exceptions.UserAlreadyExistsException;

public interface SwagSwapUserService {

	SwagSwapUser get(Long id);

	void insert(SwagSwapUser swagSwapUser) throws UserAlreadyExistsException ;
	
	void update(SwagSwapUser swagSwapUser);

	SwagSwapUser findByEmail();
	
	SwagSwapUser findByEmailOrCreate();

	void addOrUpdateRating(String userEmail, SwagItemRating swagItemRating) throws InvalidSwagItemRatingException ;
	
	public boolean isItemOwner(SwagItem item);
	
	/**
	 * Wrapped Google UserService methods
	 */
	
	public String createLoginURL(String destinationURL);
	
	public String createLogoutURL(String destinationURL);
	
	public User getCurrentUser();
	
	public boolean isUserAdmin();
	
	public boolean isUserLoggedIn();
	
	public void blackListUser(String email);

	void removeUserFromMailings(String userId);

}