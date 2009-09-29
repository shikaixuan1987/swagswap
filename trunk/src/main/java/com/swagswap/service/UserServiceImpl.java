package com.swagswap.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.appengine.api.users.UserService;
import com.swagswap.dao.UserDao;
import com.swagswap.domain.User;

public class UserServiceImpl {
	private static final Logger log = Logger.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDao userDao;
	
	public User get(Long id) {
		UserService userService;
		return userDao.get(id);
	}
	
	
	public void save(User user) {
		userDao.save(user);
	}
	
	public User findByEmail(String email) {
		return userDao.findByEmail(email);
	}
	
	/**
	 * @param email
	 * @return existing or new User
	 */
	public User findOrCreateByEmail(String email) {
		User user = userDao.findByEmail(email);
		if (user==null) {
			user = new User(email);
			save(user);
		}
		return user;
	}
	
//	public void addRating(User user, UserRating userRating){}
//		
}
