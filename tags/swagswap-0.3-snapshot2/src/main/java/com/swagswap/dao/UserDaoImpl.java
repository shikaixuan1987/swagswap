package com.swagswap.dao;

import java.util.Date;

import javax.jdo.Query;

import org.apache.log4j.Logger;
import org.springframework.orm.jdo.support.JdoDaoSupport;

import com.swagswap.domain.SwagSwapUser;

/**
 * Persistence of Users. Note: Authentication is taken care of by the 
 * Google UserService. 
 * 
 */
@SuppressWarnings("unchecked")
public class UserDaoImpl extends JdoDaoSupport implements UserDao {

	private static final Logger log = Logger.getLogger(UserDaoImpl.class);

	
	/* (non-Javadoc)
	 * @see com.swagswap.dao.UserDao#get(java.lang.Long)
	 */
	public SwagSwapUser get(Long id) {
		SwagSwapUser swagSwapUser = getPersistenceManager().getObjectById(SwagSwapUser.class, id);
		return swagSwapUser;
	}
	
	/* 
	 * I learned how to do this here: http://speedo.ow2.org/doc/jdo2qr.html
	 * @returns User if found or null if not
	 */
	public SwagSwapUser findByEmail(String email) {
		Query query = getPersistenceManager().newQuery(
				"select from " + SwagSwapUser.class.getName()
				+ " where email==p1 parameters String p1");
		query.setUnique(true);
		return (SwagSwapUser) query.execute(email); 
	}

	//Can't delete users right now (design decision :)
	// public void delete(Long id) {}

	/**
	 * Does not update SwagItemRatings. 
	 */
	public void update(SwagSwapUser updatedUser) {
		SwagSwapUser orig = get(updatedUser.getKey());
		orig.setNickName(updatedUser.getNickName());
		//This will replace any all items. 
		//TODO this gives error "object is managed by a different object manager"
		//orig.getSwagItemRatings().addAll(updatedUser.getSwagItemRatings());
	}

	public void insert(SwagSwapUser swagSwapUser) {
		Date now = new Date();
		swagSwapUser.setJoined(now);
		getPersistenceManager().makePersistent(swagSwapUser);
	}
	
}