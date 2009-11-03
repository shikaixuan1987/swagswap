package com.swagswap.dao.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.cache.Cache;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.swagswap.dao.ItemDao;
import com.swagswap.domain.SwagItem;
import com.swagswap.domain.SwagItemComment;

@SuppressWarnings("unchecked")
public class ItemCacheManager implements ItemDao, InitializingBean {

	private static final Logger log = Logger.getLogger(ItemCacheManager.class);

	@Autowired
	private SwagCacheManager swagCacheManager;

	private ItemDao itemDao;

	/**
	 * Maintain list of cache keys for more efficient getAll()
	 */
	private List<Long> keyList;

	public ItemCacheManager() {
	}

	// for unit tests
	protected ItemCacheManager(ItemDao itemDao,
			SwagCacheManager swagCacheManager) {
		this();
		this.itemDao = itemDao;
	}

	public void setItemDao(ItemDao itemDao) {
		this.itemDao = itemDao;
	}

	public void afterPropertiesSet() throws Exception {
		loadSwagItems();
	}

	private void loadSwagItems() {
		log.info("Inserting all Swag Items into cache from DAO");

		keyList = new ArrayList<Long>();

		List<SwagItem> swagItemList = itemDao.getAll();
		Iterator<SwagItem> iter = swagItemList.iterator();
		while (iter.hasNext()) {
			SwagItem item = iter.next();
			swagCacheManager.getCache().put(item.getKey(), item);
			keyList.add(item.getKey());
		}
		log.info(swagItemList.size() + " Swag Items inserted to cache");
	}

	public List<SwagItem> getAll() {


		List<SwagItem> swagList = new ArrayList<SwagItem>();
		Map<Long, SwagItem> allItems = swagCacheManager.getCache().getAll(keyList);
				
		List myList = new ArrayList(allItems.values());
		Collections.reverse(myList);
		Iterator<SwagItem> iter = myList.iterator();
		while (iter.hasNext()) {
			SwagItem item = iter.next();
			swagList.add(item);
		}

		return swagList;

	}

	public void addComment(SwagItemComment swagItemComment) {
		itemDao.addComment(swagItemComment);
		// Only refresh affected item
		refreshItemInCache(swagItemComment.getSwagItemKey());
	}

	public void delete(Long id) {
		itemDao.delete(id);
		// Also remove from cache
		Cache cache = swagCacheManager.getCache();
		if (cache.containsKey(id)) {
			SwagItem item = ((SwagItem) cache.get(id));
			// And delete imageKey from Cache
			String imageKey = item.getImageKey();
			if (cache.containsKey(imageKey)) {
				cache.remove(imageKey);
			}
			cache.remove(id);
			keyList.remove(id);
		} else {
			// Refresh is things were out of sync
			log.warn("Expected Swag Item not found in Cache.  Key " + id
					+ ".  Refresh cache");
			loadSwagItems();
		}

	}

	public SwagItem get(Long id) {
		return get(id, false);
	}

	public SwagItem get(Long id, boolean loadSwagImage) {

		// TODO More efficient to use try/catch than containsKey test
		if (swagCacheManager.getCache().containsKey(id)) {
			// returned cached swagItem
			return (SwagItem) swagCacheManager.getCache().get(id);
		}
		log.warn("Expected Swag Item not found in Cache.  Key " + id
				+ ".  Refresh cache");
		// Not in cache so refresh cache from DAO
		loadSwagItems();
		return (SwagItem) swagCacheManager.getCache().get(id);
	}

	public void insert(SwagItem swagItem) {
		itemDao.insert(swagItem);
		// Add new item to cache and keyList
		swagCacheManager.getCache().put(swagItem.getKey(), swagItem);
		keyList.add(swagItem.getKey());
		log.info("New SwagItem " + swagItem.getKey() + " added to cache");
	}

	public List<SwagItem> search(String searchString) {
		// TODO Think about implementation of this. Use DAO for now.
		return itemDao.search(searchString);
	}

	public void update(SwagItem updatedItem) {

		Cache cache = swagCacheManager.getCache();
		if (cache.containsKey(updatedItem.getKey())) {
			cache.remove(updatedItem.getImageKey());
		}

		itemDao.update(updatedItem);
		// Refresh affected item
		refreshItemInCache(updatedItem.getKey());
	}

	public void updateRating(Long swagItemKey, int computedRatingDifference,
			boolean isNewRating) {
		itemDao
				.updateRating(swagItemKey, computedRatingDifference,
						isNewRating);
		refreshItemInCache(swagItemKey);
	}

	private void refreshItemInCache(Long key) {
		log.info("Refreshing Swag Item in Cache.  Key " + key);
		// TODO More efficient to use try/catch than containsKey test
		if (swagCacheManager.getCache().containsKey(key)) {
			swagCacheManager.getCache().put(key, itemDao.get(key, true));
		} else {
			log.warn("Expected Swag Item not found in Cache.  Key " + key
					+ ".  Refresh cache");
			loadSwagItems();
		}
	}

	public List<SwagItem> findByTag(String searchString) {
		// TODO Implement this
		return itemDao.findByTag(searchString);
	}

}
