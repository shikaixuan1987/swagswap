package com.swagswap.dao.cache;

import com.swagswap.common.LocalDatastoreTestCase;
import com.swagswap.dao.ImageDaoImpl;
import com.swagswap.service.ImageServiceImpl;

public class ImageCacheManagerTest extends LocalDatastoreTestCase {
	
	private ImageCacheManager imageCacheManager;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		if (imageCacheManager == null) {
			ImageDaoImpl imageDao = new ImageDaoImpl();
			imageDao.setPersistenceManagerFactory(PMF);
			imageCacheManager = new ImageCacheManager(imageDao);
		}
	}
	
	


}
