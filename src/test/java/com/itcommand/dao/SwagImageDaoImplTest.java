package com.itcommand.dao;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query;
import com.itcommand.domain.SwagImage;
import com.itcommand.domain.SwagItem;
import com.itcommand.test.Fixture;
import com.itcommand.test.LocalDatastoreTestCase;


public class SwagImageDaoImplTest extends LocalDatastoreTestCase  {
	
	private SwagItemDaoImpl swagItemDao;
	private SwagImageDaoImpl swagImageDao;
	
	@Override
    public void setUp() throws Exception {
        super.setUp();
        if (swagItemDao == null) {
    		SwagItemDaoImpl swagItemDao = new SwagItemDaoImpl();
            swagItemDao.setPersistenceManagerFactory(PMF);
    		this.swagItemDao=swagItemDao;
        }
        if (swagImageDao == null) {
        	SwagImageDaoImpl swagImageDao = new SwagImageDaoImpl();
        	swagImageDao.setPersistenceManagerFactory(PMF);
        	this.swagImageDao=swagImageDao;
        }
	}

    public void testGet() {
    	
        SwagItem swagItem = Fixture.createSwagItem();
        swagItemDao.save(swagItem);
        
        SwagImage image = swagImageDao.get(swagItem.getImageKey());
        assertNotNull(image);
    }


    /**
     * Database count assertions
     * 
     * @param swagItemsExpected
     * @param swagImagesExpected
     */
	private void assertNumberOfItemsAndImages(int swagItemsExpected, int swagImagesExpected) {
		Query query = new Query(SwagItem.class.getSimpleName());
    	assertEquals(swagItemsExpected, DatastoreServiceFactory.getDatastoreService().prepare(query).countEntities());
    	
    	query = new Query(SwagImage.class.getSimpleName());
    	assertEquals(swagImagesExpected, DatastoreServiceFactory.getDatastoreService().prepare(query).countEntities());
	}
}
