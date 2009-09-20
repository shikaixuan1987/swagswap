package com.swagswap.dao;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.dev.LocalDatastoreService;
import com.google.appengine.tools.development.ApiProxyLocalImpl;
import com.google.apphosting.api.ApiProxy;
import com.swagswap.common.Fixture;
import com.swagswap.common.LocalDatastoreTestCase;
import com.swagswap.dao.SwagItemDaoImpl;
import com.swagswap.domain.SwagImage;
import com.swagswap.domain.SwagItem;


public class SwagItemDaoImplTest extends LocalDatastoreTestCase  {
	
	private SwagItemDaoImpl swagItemDao;
	
	@Override
    public void setUp() throws Exception {
        super.setUp();
        if (swagItemDao == null) {
    		SwagItemDaoImpl swagItemDao = new SwagItemDaoImpl();
            swagItemDao.setPersistenceManagerFactory(PMF);
    		this.swagItemDao=swagItemDao;
        }
	}

    public void testSave() {
        
        SwagItem swagItem = Fixture.createSwagItem();
        swagItemDao.save(swagItem);

        assertNumberOfItemsAndImages(1,1);    
    }
    
    /**
     * Make sure if they're no image, that no image is created
     */
    public void testSaveNoImage() {
    	
    	SwagItem swagItem = Fixture.createSwagItem();
    	swagItem.setImage(null);
    	swagItemDao.save(swagItem);
    	
    	assertNumberOfItemsAndImages(1,0);
//        assertEquals(originalSwagItem,savedItem);
    }

    /**
     * Make sure all fields are saved and that (owned one-to-many relationship)
     * SwagItem is lazy loaded
     */
    public void testGet() {
    	
        SwagItem originalItem = Fixture.createSwagItem();
        swagItemDao.save(originalItem);
        
        SwagItem retrievedItem = swagItemDao.get(originalItem.getKey());
        assertEquals(originalItem,retrievedItem);
        
        //Image is lazy loaded but make sure it's available when we access it
        SwagImage image = retrievedItem.getImage();
        assertNotNull(image);
    }

    /**
     * Make sure SwagImages are deleted too!
     */
    public void testDelete() {
        
        SwagItem swagItem = Fixture.createSwagItem();
        
        swagItemDao.save(swagItem);
        swagItemDao.delete(swagItem.getKey());
        
        assertNumberOfItemsAndImages(0,0);
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
