package com.swagswap.dao;

import com.swagswap.domain.SwagSwapUser;

public interface UserDao {

	SwagSwapUser get(Long id);

	SwagSwapUser findByEmail(String email);

	void update(SwagSwapUser updatedUser);

	void insert(SwagSwapUser swagSwapUser);

}