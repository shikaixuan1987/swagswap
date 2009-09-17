package com.itcommand.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.log4j.Logger;
import org.springframework.orm.jdo.support.JdoDaoSupport;

import com.itcommand.domain.SwagImage;
import com.itcommand.domain.SwagItem;

/**
 * (from spring docs): JdoTemplate will ensure that PersistenceManagers are
 * properly opened and closed, and automatically participate in transactions.
 * 
 * No need for finally blocks that close the persistenceManager!
 * 
 */
public class SwagItemDaoImpl extends JdoDaoSupport implements SwagItemDao {

	private static final Logger log = Logger.getLogger(SwagItemDaoImpl.class);

	public SwagItem get(Long id) {
		PersistenceManager pm = getPersistenceManager();
		SwagItem swagItem = pm.getObjectById(SwagItem.class, id);
		return swagItem;
	}

	@SuppressWarnings("unchecked")
	public Collection<SwagItem> search(String queryString) {
		PersistenceManager pm = getPersistenceManager();
		Query query = pm.newQuery(SwagItem.class);
		query.setOrdering("name");
		query.setFilter("name >= id && name < id2");
		query.declareParameters("String id, String id2");

		Collection<SwagItem> swagItems = pm.detachCopyAll((Collection<SwagItem>) query.execute(queryString, queryString
				+ "\ufffd"));

		return swagItems;

		// TODO change to something like
		// public Collection loadProductsByCategory(final String category)
		// throws DataAccessException {
		// return (Collection) this.jdoTemplate.execute(new JdoCallback() {
		// public Object doInJdo(PersistenceManager pm) throws JDOException {
		// Query query = pm.newQuery(Product.class, "category = pCategory");
		// query.declareParameters("String pCategory");
		// List result = query.execute(category);
		// // do some further stuff with the result list
		// return result;
		// }
		// });
		// }

	}

	@SuppressWarnings("unchecked")
	public List<SwagItem> getAll() {
		PersistenceManager pm = getPersistenceManager();
		String query = "select from " + SwagItem.class.getName() + " order by name";
		List<SwagItem> swagItems = (List<SwagItem>) pm.newQuery(query).execute();

		if (log.isInfoEnabled()) {
			log.info("returning " + swagItems.size() + " swag items");
			for (SwagItem swagItem : swagItems) {
				log.info(swagItem.getName());
			}
		}
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
	 * Swag images (children) of SwagItems are not automatically deleted
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
		//TODO delete old image if it's changed
		
		SwagItem orig = getPersistenceManager().getObjectById(SwagItem.class, updatedItem.getKey());
		orig.setName(updatedItem.getName());
		orig.setDescription(updatedItem.getDescription());
		orig.setImageKey(updatedItem.getImageKey());
		orig.setImage(updatedItem.getImage());
		orig.setOwner(updatedItem.getOwner());
		orig.setRating(updatedItem.getRating());
		orig.setLastUpdated(new Date());
		orig.setTags(updatedItem.getTags());
		orig.setComments(updatedItem.getComments());
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
		
		getPersistenceManager().makePersistent(swagItem);
		// Save image key here in the parent 
		// See comment in SwagItem above the field imageKey
		if (swagItem.getImage()!=null) {
			swagItem.setImageKey(swagItem.getImage().getEncodedKey());
		}
	}

}