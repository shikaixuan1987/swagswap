package com.swagswap.dao.cache;

import java.util.List;

import javax.jdo.JDOObjectNotFoundException;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query;
import com.swagswap.common.Fixture;
import com.swagswap.common.LocalDatastoreTestCase;
import com.swagswap.dao.ItemDaoImpl;
import com.swagswap.domain.SwagImage;
import com.swagswap.domain.SwagItem;

public class ItemCacheManagerTest extends LocalDatastoreTestCase {

	private ItemCacheManager itemCacheManager;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		if (itemCacheManager == null) {
			ItemDaoImpl itemDao = new ItemDaoImpl();
			itemDao.setPersistenceManagerFactory(PMF);
			itemCacheManager = new ItemCacheManager(itemDao);
		}
	}

	public void testInsert() {

		SwagItem swagItem = Fixture.createSwagItem();
		itemCacheManager.insert(swagItem);

		assertNumberOfItemsAndImages(1, 1);
	}

	public void testUpdate() {
		SwagItem orig = Fixture.createSwagItem();
		itemCacheManager.insert(orig);

		orig.setName("new name");
		itemCacheManager.update(orig);

		SwagItem retrieved = itemCacheManager.get(orig.getKey());
		assertEquals(orig.getName(), retrieved.getName());
	}

	public void testGet() {

		SwagItem originalItem = Fixture.createSwagItem();
		itemCacheManager.insert(originalItem);

		SwagItem retrievedItem = itemCacheManager.get(originalItem.getKey());
		assertEquals(originalItem, retrievedItem);

		// Image is lazy loaded but make sure it's available when we access it
		SwagImage image = retrievedItem.getImage();
		assertNotNull(image);
	}

	public void testGetAll() {

		SwagItem item1 = Fixture.createSwagItem();
		itemCacheManager.insert(item1);

		SwagItem item2 = Fixture.createSwagItem();
		itemCacheManager.insert(item2);

		List<SwagItem> retrievedItems = itemCacheManager.getAll();
		assertEquals(retrievedItems.size(), 2);
	}

	public void testDelete() {

		SwagItem item1 = Fixture.createSwagItem();
		itemCacheManager.insert(item1);

		itemCacheManager.delete(item1.getKey());
		try {
			SwagItem retrievedItem = itemCacheManager.get(item1.getKey());
			fail("Should have thrown a JDOObjectNotFoundException");
		} catch (JDOObjectNotFoundException e) {
			// good
		}
	}

	public void testGetNonExistantItem() {
		try {
			itemCacheManager.get(1000L); // assume non-existant key
			fail("Should have thrown a JDOObjectNotFoundException");
		} catch (JDOObjectNotFoundException e) {
			// good
		}
	}

	/**
	 * Database count assertions
	 * 
	 * @param swagItemsExpected
	 * @param swagImagesExpected
	 */
	private void assertNumberOfItemsAndImages(int swagItemsExpected,
			int swagImagesExpected) {
		Query query = new Query(SwagItem.class.getSimpleName());
		assertEquals(swagItemsExpected, DatastoreServiceFactory
				.getDatastoreService().prepare(query).countEntities());

		query = new Query(SwagImage.class.getSimpleName());
		assertEquals(swagImagesExpected, DatastoreServiceFactory
				.getDatastoreService().prepare(query).countEntities());
	}

}
