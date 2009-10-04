package com.swagswap.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.compass.core.CompassHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.swagswap.dao.ItemDao;
import com.swagswap.domain.SwagItem;
import com.swagswap.indexing.SwagSwapCompass;

/**
 * For transactionality and will be used for caching.
 * @author sam
 *
 */
public class ItemServiceImpl implements ItemService {

	private static final Logger log = Logger.getLogger(ItemServiceImpl.class);

	@Autowired
	private ItemDao itemDao;
	@Autowired
	private UserService googleUserService;
	@Autowired
	private SwagSwapUserService swagSwapUserService; //for saving users to our app
	@Autowired
	private SwagSwapCompass swagSwapCompass; //Compass instance for searching

	public ItemServiceImpl() {
	}

	//for unit tests
	protected ItemServiceImpl(ItemDao itemDao) {
		this.itemDao=itemDao;
	}

	/**
	 * Load swagItem, but not associated swagImage
	 */
	public SwagItem get(Long id) {
		return get(id, false);
	}
	
	/**
	 * 
	 * @param id
	 * @param loadSwagImage whether to load swagImage (it is lazy loaded by JDO)
	 * @return SwagItem if found
	 * @throws Exception if item not found
	 */
	public SwagItem get(Long id, boolean loadSwagImage) {
		return itemDao.get(id, loadSwagImage);
	}
	
	/*
	//This is old impl. We'll use compass instead since GAE doesn't support
	//case-insensitive queries (yet)
	public Collection<SwagItem> search(String queryString) {
		return itemDao.search(queryString);
	}
	*/
	public Collection<SwagItem> search(String queryString) {
		CompassHits hits = swagSwapCompass.getCompass().openSearchSession().find("*"+queryString+"*");
		Set<SwagItem> swagItems = new HashSet<SwagItem>();
		for (int i = 0; i < hits.length(); i++) {
			swagItems.add((SwagItem)hits.data(i));
			
		}
		return swagItems;
	}

	public List<SwagItem> getAll() {
		return itemDao.getAll();
	}

	/**
	 * saves swag item and image (image is saved in dao because it's a child object)
	 * also creates new user (using swagSwapUserService if they don't already exist.)
	 * Note Because SwagSwapUser is not in the same entity group as SwagItem
	 * (I couldn't get a many-to-one relationship going in JDO), 
	 * swagSwapUserService.insert is also marked as @Transactional REQUIRES_NEW
	 */
	
	/**
	 * With Transactional Propagation.REQUIRES_NEW I get
	 * 
	 * can't operate on multiple entity groups in a single transaction. found both Element {
		type: "SwagItem"
		id: 5
		}
		and Element {
		type: "index"
		name: "index-index-swagitem"
		}
        http://forum.compass-project.org/thread.jspa?threadID=216145
	 */
//	@Transactional(readOnly = false, propagation = Propagation.SUPPORTS)
	public void save(SwagItem swagItem) {
		if (swagItem.isNew()) {
			String currentUserEmail = googleUserService.getCurrentUser().getEmail();
			swagItem.setOwnerEmail(currentUserEmail);
			String currentUserNickName = googleUserService.getCurrentUser().getNickname();
			swagItem.setOwnerNickName(currentUserNickName);
			itemDao.insert(swagItem);
			/**
			 * No need to create swagSwapUser here. We only need a user in our DB
			 * if they rate something (to remember their rating)
			 * Anyway, this call fails since it tries to update two different entity 
			 * groups in one transaction
			 */
			//swagSwapUserService.findByEmailOrCreate(currentUserEmail);
		}
		else { //update
			checkPermissions(swagItem.getKey());
			itemDao.update(swagItem);
		}
		
		//to test transactions, uncomment the throw exception line below
		//and try this method with and without the annotation
		//throw new RuntimeException("see if it rolls back");
	}
	
	@Transactional(readOnly = false, propagation = Propagation.SUPPORTS)
	public synchronized void updateRating(Long swagItemKey, int computedRatingDifference, boolean isNewRating) {
		SwagItem swagItem = get(swagItemKey);
		itemDao.updateRating(swagItemKey, computedRatingDifference, isNewRating);
	}

	public void delete(Long id) {
		checkPermissions(id);
		itemDao.delete(id);
	}

	public void setSwagItemDao(ItemDao itemDao) {
		this.itemDao = itemDao;
	}

	public void setGoogleUserService(UserService googleUserService) {
		this.googleUserService = googleUserService;
	}
	
	public void setSwagSwapUserService(SwagSwapUserService swagSwapUserService) {
		this.swagSwapUserService = swagSwapUserService;
	}
	
	/**
	 * @param swagItemIdToCheck
	 * @throws AccessDeniedException if currentUser is not allowed to access an item
	 */
	private void checkPermissions(Long swagItemIdToCheck) throws AccessDeniedException {
		User user = googleUserService.getCurrentUser();
		//admins can do everything!
		if (googleUserService.isUserAdmin()) {
			return;
		}
		// Not logged in 
		// (if this happened the webapp messed up or someone's trying to hack us)
		if (user==null) {
			throw new AccessDeniedException("User not logged in.  No permissions");
		}
		//get item fresh to prevent client side attacks
		SwagItem swagItemToCheck = get(swagItemIdToCheck); 
		// Their email doesn't match the swagItem 
		// (again, if this happened the webapp messed up or someone's trying to hack us)
		if (!user.getEmail().equals(swagItemToCheck.getOwnerEmail())) {
			throw new AccessDeniedException(
					swagItemToCheck,
					user.getEmail());
		}
	}
}