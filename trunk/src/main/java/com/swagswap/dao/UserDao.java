package com.swagswap.dao;

import com.swagswap.domain.User;

public interface UserDao {

	public abstract User get(Long id);

	public abstract User findByEmail(String email);

	public abstract void save(User user);

}