package com.swagswap.dao;

import java.util.List;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query;
import com.swagswap.common.Fixture;
import com.swagswap.common.LocalDatastoreTestCase;
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
     * Make sure if there's no image, that an empty one is created
     * Otherwise with JDO 1-to-1 owned relationships you can't
     * add one later.  TODO ask about this on mailing list
     */
    public void testSaveNoImage() {
    	
    	SwagItem swagItem = Fixture.createSwagItem();
    	swagItem.setImage(null);
    	swagItemDao.save(swagItem);
    	
    	assertNumberOfItemsAndImages(1,1);
    }
    
    //TODO Why is this failing?
    public void testSaveReplaceImage() {
    	
    	SwagItem swagItem1 = Fixture.createSwagItem();
    	swagItemDao.save(swagItem1);
    	SwagImage oldImage=swagItem1.getImage();
    	
    	//Make a new SwagItem so the images reference aren't pointing to the same thing
    	SwagItem swagItem2 = swagItemDao.get(swagItem1.getKey());
    	//This is how we indicate a new image is coming in
    	swagItem2.setImageBytes(new byte[]{8,7,6,5,4,3,2,1});
    	swagItemDao.save(swagItem2);
    	swagItem2 = swagItemDao.get(swagItem1.getKey());
    	SwagImage newImage = swagItem2.getImage();
    	
    	assertNotEquals(oldImage, newImage);
    	
    }
    
    // Strings over 500 chars have to be stored in a 
    // com.google.appengine.api.datastore.Text
    public void testLargeDescription() {
    	
    	SwagItem originalItem = Fixture.createSwagItem();
    	String randomDescription = Fixture.get510Chars();
    	originalItem.setDescription(randomDescription);
    	swagItemDao.save(originalItem);
    	assertNumberOfItemsAndImages(1,1);  
    	
        SwagItem retrievedItem = swagItemDao.get(originalItem.getKey());
        
    	assertEquals(randomDescription, retrievedItem.getDescription());
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
     * Should be ordered by lastUpdated timestamp
     * @throws Exception 
     */
    public void testGetAllOrdering() throws Exception {
    	
        SwagItem item0 = Fixture.createSwagItem();
        item0.setName("b");
        swagItemDao.save(item0);
        
        Thread.currentThread().sleep(1000); //make it wait so they have different timestamps
        SwagItem item1 = Fixture.createSwagItem();
        item1.setName("a");
        swagItemDao.save(item1);
        
        Thread.currentThread().sleep(1000); //make it wait so they have different timestamps
        SwagItem item2 = Fixture.createSwagItem();
        item2.setName("c");
        swagItemDao.save(item2);
        
        List<SwagItem> items = swagItemDao.getAll();
        
        assertEquals(item2, items.get(0));
        assertEquals(item1, items.get(1));
        assertEquals(item0, items.get(2));
        
        //TODO why is this not working?
//        //now save the middle one and hope that it comes out first
//        swagItemDao.save(item1);
//        items = swagItemDao.getAll();
//        
//        assertEquals(item1, items.get(0));
        
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
    
    public void testFindByTag() {
        SwagItem swagItem = Fixture.createSwagItem();
        
        swagItemDao.save(swagItem);
        List<SwagItem> swagItems = swagItemDao.findByTag((swagItem.getTags().get(0)));
        assertEquals(swagItem.getName(), swagItems.get(0).getName());
    }
    
    public void testFindByName() {
    	SwagItem swagItem = Fixture.createSwagItem();
    	
    	swagItemDao.save(swagItem);
    	List<SwagItem> swagItems = swagItemDao.findByName(swagItem.getName());
    	assertEquals(swagItem.getName(), swagItems.get(0).getName());
    }
    
    public void testSearch() {
    	SwagItem swagItem1 = Fixture.createSwagItem();
    	swagItemDao.save(swagItem1);
    	
    	SwagItem swagItem2 = Fixture.createSwagItem();
    	swagItem2.setName("name2"); //different name than swagItem1
    	//make tag with the same name as item 1
    	swagItem2.getTags().add(swagItem1.getName());
    	swagItemDao.save(swagItem2);
    	
    	List<SwagItem> swagItems = swagItemDao.search(swagItem1.getName());
    	//expect a hit on swagItem1.name and swagItem2.tag
    	assertEquals(2, swagItems.size());
    }
    
    public void testSearch_ensure_no_duplicates() {
    	//make item with a tag that matches name
    	SwagItem swagItem = Fixture.createSwagItem();
    	swagItem.getTags().add(swagItem.getName());
    	swagItemDao.save(swagItem);
    	
    	List<SwagItem> swagItems = swagItemDao.search(swagItem.getName());
    	assertEquals(1, swagItems.size());
    }
    
    // If all tags were filled then empty search String 
    // didn't include item
    public void testEmptySearchString() {
    	SwagItem swagItem = Fixture.createSwagItem();
    	swagItem.getTags().add("another tag");
    	swagItemDao.save(swagItem);
    	
    	assertEquals(1, swagItemDao.search("").size());
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
