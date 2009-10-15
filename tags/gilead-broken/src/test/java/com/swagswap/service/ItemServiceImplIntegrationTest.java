package com.swagswap.service;

import com.google.appengine.api.users.UserServiceFactory;
import com.swagswap.common.Fixture;
import com.swagswap.common.LocalDatastoreTestCase;
import com.swagswap.dao.ItemDao;
import com.swagswap.dao.ItemDaoImpl;
import com.swagswap.domain.SwagItem;

public class ItemServiceImplIntegrationTest extends LocalDatastoreTestCase  {
	
	private ItemDao itemDao;
	private SwagSwapUserService swagSwapUserService;
	private ItemServiceImpl itemService;
	
	@Override
    public void setUp() throws Exception {
        super.setUp();
        if (itemDao == null) {
        	ItemDaoImpl itemDao = new ItemDaoImpl();
        	itemDao.setPersistenceManagerFactory(PMF);
        	this.itemDao=itemDao;
        }
        // These two services use each other which is fine but it makes test
        // setup weird
        if (swagSwapUserService == null) {
        	if (itemService==null) {
        		itemService = new ItemServiceImpl(itemDao);
        	}
        	//don't need userDao here
        	swagSwapUserService = new SwagSwapUserServiceImpl(
        	null, itemService,UserServiceFactory.getUserService());
        	
        	itemService.setSwagSwapUserService(swagSwapUserService);
        }
        

	}

	/**
	 * Make sure original image is preserved when we save with no new image
	 */
	//TODO fix me (see TODO in setUp()
    public void testSave_item_with_exiting_image_and_no_new_image() {
        
        SwagItem swagItem = Fixture.createSwagItem();
        itemService.save(swagItem);
        
        SwagItem swagItemWithNoImage = Fixture.createSwagItemNoImage();
        //pretend this is our original image getting updated
        swagItemWithNoImage.setKey(swagItem.getKey());
        itemService.save(swagItemWithNoImage);
        
        //Make sure original image still exists
        SwagItem retrieved = itemService.get(swagItemWithNoImage.getKey());
        assertNotNull(retrieved.getImage().getImage());
    }
    
    //TODO test save exceptions
 
    
}
