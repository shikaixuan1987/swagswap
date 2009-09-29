package com.swagswap.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.commons.collections.list.SetUniqueList;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.orm.jdo.support.JdoDaoSupport;

import com.google.appengine.api.datastore.Blob;
import com.swagswap.domain.SwagImage;
import com.swagswap.domain.SwagItem;
import com.swagswap.domain.User;

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
	public User get(Long id) {
		User user = getPersistenceManager().getObjectById(User.class, id);
		return user;
	}
	
	/* 
	 * I learned how to do this here: http://speedo.ow2.org/doc/jdo2qr.html
	 * @returns User if found or null if not
	 */
	public User findByEmail(String email) {
		Query query = getPersistenceManager().newQuery(
				"select from " + User.class.getName()
				+ " where email==p1 parameters String p1");
		query.setUnique(true);
		return (User) query.execute(email); 
	}

	/* (non-Javadoc)
	 * @see com.swagswap.dao.UserDao#save(com.swagswap.domain.User)
	 */
	public void save(User user) {
		if (user.isNew()) {
			insert(user);
		} else {
			update(user);
		}
	}

	//Can't delete users right now (design decision :)
//	public void delete(Long id) {}

	private void update(User updatedUser) {
		User orig = get(updatedUser.getKey());
		orig.setEmail(updatedUser.getEmail());
		orig.setNickName(updatedUser.getNickName());
		orig.setRatedSwagItems(updatedUser.getRatedSwagItems());
	}

	private void insert(User user) {
		Date now = new Date();
		user.setJoined(now);
		getPersistenceManager().makePersistent(user);
	}
	
}