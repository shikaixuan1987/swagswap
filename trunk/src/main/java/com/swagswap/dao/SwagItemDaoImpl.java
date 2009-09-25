package com.swagswap.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.commons.collections.list.SetUniqueList;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.orm.jdo.support.JdoDaoSupport;

import com.google.appengine.api.datastore.Blob;
import com.swagswap.domain.SwagImage;
import com.swagswap.domain.SwagItem;

/**
 * Persistence of SwagItems using Spring JDO
 * (from spring docs): JdoTemplate will ensure that PersistenceManagers are
 * properly opened and closed, and automatically participate in transactions.
 * 
 * No need for finally blocks that close the persistenceManager!
 * 
 */
@SuppressWarnings("unchecked")
public class SwagItemDaoImpl extends JdoDaoSupport implements SwagItemDao {

	private static final Logger log = Logger.getLogger(SwagItemDaoImpl.class);

	/**
	 * Load swagItem, but not associated swagImage
	 */
	public SwagItem get(Long id) {
		return get(id, false);
	}
	
	/**
	 * 
	 * @param id
	 * @param loadSwagImage whether to load swagImage (it is lazy loaded by GAE)
	 * @return SwagItem if found
	 * @throws JDOObjectNotFoundException (RuntimeException) if item not found
	 */
	public SwagItem get(Long id, boolean loadSwagImage) {
		PersistenceManager pm = getPersistenceManager();
		SwagItem swagItem = pm.getObjectById(SwagItem.class, id);
		if (loadSwagImage) {
			swagItem.getImage();
		}
		return swagItem;
	}
	
	/**
	 * Search by tag and by name
	 * NOTE: only supports case sensitive queries
	 * This implementation searches for exact SwagItem.name or exact tag match
	 * @param searchString
	 */
    public List<SwagItem> search(String searchString) {
    	if (StringUtils.isEmpty(searchString)) {
    		return getAll();
    	}
        List<SwagItem> tagResults = findByTag(searchString);
        List<SwagItem> nameResults = findByName(searchString);
        //commons-lang trickery to ensure uniqueness in a list
        List<SwagItem> allResults = SetUniqueList.decorate(new ArrayList<SwagItem>());
        allResults.addAll(tagResults);
        allResults.addAll(nameResults);
        return allResults;
    }


    // Figured these out from this page:
    // http://code.google.com/p/datanucleus-appengine/source/browse/trunk/tests/org/datanucleus/store/appengine/query/JDOQLQueryTest.java#2178
	protected List<SwagItem> findByTag(String searchString) {
		Query query = getPersistenceManager().newQuery(
                "select from " + SwagItem.class.getName()
                + " where tags.contains(p1) parameters String p1");
        return (List<SwagItem>) query.execute(searchString); 
	}
	
	protected List<SwagItem> findByName(String searchString) {
		Query query = getPersistenceManager().newQuery(
				"select from " + SwagItem.class.getName()
				+ " where name==p1 parameters String p1");
		return (List<SwagItem>) query.execute(searchString); 
	}
	
	public List<SwagItem> getAll() {
		PersistenceManager pm = getPersistenceManager();
		String query = "select from " + SwagItem.class.getName() + " order by lastUpdated desc";
		List<SwagItem> swagItems = (List<SwagItem>) pm.newQuery(query).execute();
		logSwagItems(swagItems); //debugging output
		return swagItems;

	}

	public void save(SwagItem swagItem) {
		if (swagItem.isNew()) {
			insert(swagItem);
		} else {
			update(swagItem);
		}
	}

	/**
	 * SwagImages (children) of SwagItems are automatically deleted
	 * So you have to do that yourself
	 * see http://code.google.com/appengine/docs/python/datastore/keysandentitygroups.html#Entity_Groups_Ancestors_and_Paths
	 */
	public void delete(Long id) {
		PersistenceManager pm = getPersistenceManager();
		SwagItem swagItem = pm.getObjectById(SwagItem.class, id);
		SwagImage swagImage = swagItem.getImage();
		pm.deletePersistent(swagImage);
		pm.deletePersistent(swagItem);
	}

	/**
	 * @param updatedItem
	 * TODO take care of image here
	 */
	private void update(SwagItem updatedItem) {
		SwagItem orig = get(updatedItem.getKey(),true);
		orig.setName(updatedItem.getName());
		orig.setDescription(updatedItem.getDescription());
		orig.setOwner(updatedItem.getOwner());
		orig.setRating(updatedItem.getRating());
		orig.setLastUpdated(new Date());
		orig.setTags(updatedItem.getTags());
		orig.setComments(updatedItem.getComments());
		if (updatedItem.hasNewImage()) { //replace existing image
			//The following line doesn't work! You have to operate on the stored SwagImage
			//orig.setImage(updatedItem.getImage());
			orig.getImage().setImage(new Blob(updatedItem.getImageBytes()));
		}
	}
	
	/**
	 * Note: swagItem ref passed in is updated 
	 * @param swagItem
	 */
	private void insert(SwagItem swagItem) {
		Date now = new Date();
		swagItem.setCreated(now);
		swagItem.setLastUpdated(now);
		// TODO DI me
		// UserService userService = UserServiceFactory.getUserService();
		// swagItem.setOwner(userService.getCurrentUser().getEmail());
		swagItem.setNumberOfRatings(0);
		if (swagItem.hasNewImage()) {
			swagItem.setImage(new SwagImage(swagItem.getImageBytes()));
		}
		else {
			//add empty image, otherwise JDO won't allow you to add one
			//a child later
			swagItem.setImage(new SwagImage());
		}
		getPersistenceManager().makePersistent(swagItem);
		// Save image key here in the parent 
		// See comment in SwagItem above the field imageKey
		swagItem.setImageKey(swagItem.getImage().getEncodedKey());
	}
	

	private void logSwagItems(List<SwagItem> swagItems) {
		if (log.isDebugEnabled()) {
			log.debug("returning " + swagItems.size() + " swag items");
			for (SwagItem swagItem : swagItems) {
				log.debug(swagItem.getName() + " key: " + swagItem.getKey() + " Time: " + swagItem.getLastUpdated());
			}
		}
	}
	
}