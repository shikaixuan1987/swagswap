package com.swagswap.service;

import com.swagswap.domain.SwagItemRating;
import com.swagswap.domain.SwagSwapUser;
import com.swagswap.exceptions.UserAlreadyExistsException;

public interface SwagSwapUserService {

	SwagSwapUser get(Long id);

	void insert(SwagSwapUser swagSwapUser) throws UserAlreadyExistsException ;
	
	void update(SwagSwapUser swagSwapUser);

	SwagSwapUser findByEmail(String email);
	
	SwagSwapUser findByEmailOrCreate(String email);

	void addOrUpdateRating(String userEmail, SwagItemRating swagItemRating);

}