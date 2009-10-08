package com.swagswap.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.swagswap.dao.ItemDao;
import com.swagswap.domain.SwagItem;
import com.swagswap.exceptions.AccessDeniedException;
import com.swagswap.exceptions.ImageTooLargeException;
import com.swagswap.exceptions.InvalidSwagItemException;
import com.swagswap.exceptions.LoadImageFromURLException;

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
	
	//GAE doesn't support case-insensitive queries (yet)
	public Collection<SwagItem> search(String queryString) {
		return itemDao.search(queryString);
	}
	//  compass wasn't working for me
	/*
	public Collection<SwagItem> search(String queryString) {
		CompassHits hits = swagSwapCompass.getCompass().openSearchSession().find("*"+queryString+"*");
		Set<SwagItem> swagItems = new HashSet<SwagItem>();
		for (int i = 0; i < hits.length(); i++) {
			swagItems.add((SwagItem)hits.data(i));
			
		}
		return swagItems;
	}
	*/

	public List<SwagItem> getAll() {
		return itemDao.getAll();
	}

	/**
	 * saves swag item and image (image is saved in dao because it's a child object)
	 * (A user is not associated with a swagitem via a JDO relationship because 
	 * Icouldn't get a many-to-one relationship going in JDO), 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	
	@Transactional(readOnly = false, propagation = Propagation.SUPPORTS)
	public void save(SwagItem swagItem) 
		throws LoadImageFromURLException, ImageTooLargeException {
		if (StringUtils.isEmpty(swagItem.getName())) { //only required field
			throw new InvalidSwagItemException("name is required");
		}
		if (swagItem.isNew()) {
			String currentUserEmail = googleUserService.getCurrentUser().getEmail();
			swagItem.setOwnerEmail(currentUserEmail);
			String currentUserNickName = googleUserService.getCurrentUser().getNickname();
			swagItem.setOwnerNickName(currentUserNickName);
			
			populateSwagImage(swagItem);
			
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
			populateSwagImage(swagItem);
			itemDao.update(swagItem);
		}
		
		//to test transactions, uncomment the throw exception line below
		//and try this method with and without the annotation
		//throw new RuntimeException("see if it rolls back");
	}

	@Transactional(readOnly = false, propagation = Propagation.SUPPORTS)
	public synchronized void updateRating(Long swagItemKey, int computedRatingDifference, boolean isNewRating) {
		//TODO can this line be removed?
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
	
	private void populateSwagImage(SwagItem swagItem) 
		throws LoadImageFromURLException, ImageTooLargeException {
		if (swagItem.hasNewImageBytes()) {
			//TODO fix this comment
			// The following line only works for a save, 
			// not an upate cause there you have to operate on the stored SwagImage
			//orig.setImage(updatedItem.getImage();
			swagItem.getImage().setImage(new Blob(swagItem.getImageBytes()));
		} else if (swagItem.hasNewImageURL()) {
			populateSwagImageFromURL(swagItem);
		}
	}

	protected void populateSwagImageFromURL(SwagItem swagItem) 
		throws LoadImageFromURLException, ImageTooLargeException {
		BufferedInputStream bis=null;
		ByteArrayOutputStream bos=null;
		try {
			//fetch URL as InputStream
			URL url = new URL(swagItem.getImageURL());
			bis = new BufferedInputStream(url.openStream());
			//write it to a byte[] using a buffer since we don't know the exact image size
			byte [] buffer = new byte[1024];
			bos = new ByteArrayOutputStream();
			int i = 0;
		    while (-1 != (i = bis.read(buffer))) {
		       bos.write(buffer, 0, i);
		    }
		    byte[] imageData = bos.toByteArray();
		    if (imageData.length > 150000) {
		    	throw new ImageTooLargeException(url.toString(),150000);
		    }
			swagItem.getImage().setImage(new Blob(imageData));
		}
		catch (IOException e) {
			throw new LoadImageFromURLException(swagItem.getImageURL(), e);
		}
		finally {
			try {
				if (bis!=null) bis.close();
				if (bos!=null) bos.close();
			} catch (IOException e) {
				//ignore
			}
		}
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