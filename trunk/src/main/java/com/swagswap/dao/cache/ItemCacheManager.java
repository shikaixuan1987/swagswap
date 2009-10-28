package com.swagswap.dao.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;

import org.apache.log4j.Logger;

import com.swagswap.dao.ItemDao;
import com.swagswap.domain.SwagItem;
import com.swagswap.domain.SwagItemComment;

@SuppressWarnings("unchecked")
public class ItemCacheManager implements ItemDao {

	private static final Logger log = Logger.getLogger(ItemCacheManager.class);

	private ItemDao itemDao;
	private Cache cache;

	/**
	 * Maintain list of cache keys for more efficient getAll()
	 */
	private List<Long> keyList;

	public ItemCacheManager() {
		createCache();
	}

	// for unit tests
	protected ItemCacheManager(ItemDao itemDao) {
		this();
		this.itemDao = itemDao;
	}

	public void setItemDao(ItemDao itemDao) {
		this.itemDao = itemDao;
	}

	private void createCache() {
		log.info("Creating swagItemCache");
		cache = CacheManager.getInstance().getCache("swagItemCache");
		if (cache == null) {
			try {
				cache = CacheManager.getInstance().getCacheFactory()
						.createCache(Collections.emptyMap());
				CacheManager.getInstance()
						.registerCache("swagItemCache", cache);

			} catch (CacheException e) {
				throw new RuntimeException("Failure to create swagItemCache", e);
			}
		}
	}

	private void refreshSwagItems() {
		log.info("Inserting all Swag Items into cache from DAO");
		cache.clear();
		keyList = new ArrayList<Long>();

		List<SwagItem> swagItemList = itemDao.getAll();
		Iterator<SwagItem> iter = swagItemList.iterator();
		while (iter.hasNext()) {
			SwagItem item = iter.next();
			cache.put(item.getKey(), item);
			keyList.add(item.getKey());
		}
		log.info(swagItemList.size() + " Swag Items inserted to cache");

	}

	public List<SwagItem> getAll() {
		//  TODO.  Look at this.  Scott.
		if (keyList == null || cache.isEmpty()) {
			log.info("Cache is empty.  Refresh cache");
			refreshSwagItems();
		}
		
		List<SwagItem> swagList = new ArrayList<SwagItem>();
		Map<Long, SwagItem> allItems = cache.getAll(keyList);
		Iterator<SwagItem> iter = allItems.values().iterator();
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
		if (cache.containsKey(id)) {
			cache.remove(id);
			keyList.remove(id);
		} else {
			// Refresh is things were out of sync
			log.warn("Expected Swag Item not found in Cache.  Key " + id
					+ ".  Refresh cache");
			refreshSwagItems();
		}
	}

	public SwagItem get(Long id) {		
		return get(id, false);
	}

	public SwagItem get(Long id, boolean loadSwagImage) {
		//  TODO.  Look at this.  Scott.
		if (keyList == null || cache.isEmpty()) {
			log.info("Cache is empty.  Refresh cache");
			refreshSwagItems();
		}
		
		// TODO More efficient to use try/catch than containsKey test
		if (cache.containsKey(id)) {
			// returned cached swagItem
			return (SwagItem) cache.get(id);
		}
		log.warn("Expected Swag Item not found in Cache.  Key " + id
				+ ".  Refresh cache");
		// Not in cache so refresh cache from DAO
		refreshSwagItems();
		return (SwagItem) cache.get(id);
	}

	public void insert(SwagItem swagItem) {
		itemDao.insert(swagItem);
		//  Add new item to cache and keyList
		cache.put(swagItem.getKey(), swagItem);
		keyList.add(swagItem.getKey());

	}

	public List<SwagItem> search(String searchString) {
		// TODO Think about implementation of this. Use DAO for now.
		return itemDao.search(searchString);
	}

	public void update(SwagItem updatedItem) {
		// update using DAO
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
		if (cache.containsKey(key)) {
			cache.put(key, itemDao.get(key, true));
		} else {
			log.warn("Expected Swag Item not found in Cache.  Key " + key
					+ ".  Refresh cache");
			refreshSwagItems();
		}
	}

	public List<SwagItem> findByTag(String searchString) {
		// TODO Implement this
		return itemDao.findByTag(searchString);
	}
}
