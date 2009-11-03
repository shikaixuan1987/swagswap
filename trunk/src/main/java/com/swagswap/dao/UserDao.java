package com.swagswap.dao;

import java.util.List;

import com.swagswap.domain.BlackListedUser;
import com.swagswap.domain.SwagSwapUser;

public interface UserDao {

	SwagSwapUser get(Long id);

	SwagSwapUser findByEmail(String email);

	void update(SwagSwapUser updatedUser);

	void insert(SwagSwapUser swagSwapUser);

	void blackListUser(String email);
	
	boolean isBlackListed(String email);
	
	List<BlackListedUser> getBlackListedUsers();
}