package com.itcommand.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.log4j.Logger;
import org.springframework.orm.jdo.support.JdoDaoSupport;

import com.itcommand.domain.SwagItem;

/**
 * (from spring docs): JdoTemplate will ensure that PersistenceManagers are properly opened
 * and closed, and automatically participate in transactions. 
 *
 */
public class SwagItemDaoImpl extends JdoDaoSupport implements SwagItemDao {

	private static final Logger log = Logger.getLogger(SwagItemDaoImpl.class);

	public SwagItem get(Long id) {
		PersistenceManager pm = getPersistenceManager();
		SwagItem swagItem = null;
		try {
			swagItem = pm.getObjectById(SwagItem.class, id);

		} catch (JDOObjectNotFoundException e) {
			log.error("SwagItem not found", e);
		} finally {
			releasePersistenceManager(pm);
		}

		log.info(swagItem.getName());

		return swagItem;
	}

	@SuppressWarnings("unchecked")
	public Collection<SwagItem> search(String queryString) {
		PersistenceManager pm = getPersistenceManager();
		Query query = pm.newQuery(SwagItem.class);
		query.setOrdering("name");
		query.setFilter("name >= id && name < id2");
		query.declareParameters("String id, String id2");

		Collection<SwagItem> swagItems = pm.detachCopyAll((Collection<SwagItem>) query
				.execute(queryString, queryString + "\ufffd"));

		return swagItems;
		
		
//TODO change to something like 
//	    public Collection loadProductsByCategory(final String category) throws DataAccessException {
//	        return (Collection) this.jdoTemplate.execute(new JdoCallback() {
//	            public Object doInJdo(PersistenceManager pm) throws JDOException {
//	                Query query = pm.newQuery(Product.class, "category = pCategory");
//	                query.declareParameters("String pCategory"); 
//	                List result = query.execute(category);
//	                // do some further stuff with the result list
//	                return result;
//	            }
//	        });
//	    }

	}

	@SuppressWarnings("unchecked")
	public List<SwagItem> getAll() {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + SwagItem.class.getName()
					+ " order by name";
			List<SwagItem> swagItems = (List<SwagItem>) pm.newQuery(query)
					.execute();

			if (log.isInfoEnabled()) {
				log.info("returning " + swagItems.size() + " swag items");
				for (SwagItem swagItem : swagItems) {
					log.info(swagItem.getName());
				}
			}
			return swagItems;

		} finally {
			releasePersistenceManager(pm);
		}
	}

	public void save(SwagItem swagItem) {
		if (swagItem.isNew()) {
			insert(swagItem);
		} else {
			update(swagItem);
		}
	}

	public void delete(Long id) {
		PersistenceManager pm = getPersistenceManager();
		try {
			SwagItem swagItem = pm.getObjectById(SwagItem.class, id);
			pm.deletePersistent(swagItem);

		} catch (Exception e) {
			log.error("Error deleting swagItem");
		} finally {
			releasePersistenceManager(pm);
		}
	}

	private void update(SwagItem swagItem) {
		PersistenceManager pm = getPersistenceManager();
		try {
			SwagItem orig = pm.getObjectById(SwagItem.class, swagItem.getKey());
			orig.setName(swagItem.getName());
			orig.setDescription(swagItem.getDescription());
			orig.setImage(swagItem.getImage());
			orig.setOwner(swagItem.getOwner());
			orig.setRating(swagItem.getRating());
			orig.setLastUpdated(new Date());
			orig.setTags(swagItem.getTags());
			orig.setComments(swagItem.getComments());

		} catch (Exception e) {
			log.error("Error updating swagItem", e);
		} finally {
			releasePersistenceManager(pm);
		}
	}

	private void insert(SwagItem swagItem) {
		PersistenceManager pm = getPersistenceManager();
		try {
			Date now = new Date();
			swagItem.setCreated(now);
			swagItem.setLastUpdated(now);
			//TODO DI me
			//UserService userService = UserServiceFactory.getUserService();
			//swagItem.setOwner(userService.getCurrentUser().getEmail());
			swagItem.setNumberOfRatings(0);
			pm.makePersistent(swagItem);

		} catch (Exception e) {
			log.error("Error inserting swagItem", e);
		} finally {
			releasePersistenceManager(pm);
		}
	}

}