package com.swagswap.service;

import com.swagswap.common.Fixture;
import com.swagswap.common.LocalDatastoreTestCase;
import com.swagswap.dao.ItemDao;
import com.swagswap.dao.ItemDaoImpl;
import com.swagswap.dao.UserDaoImpl;
import com.swagswap.domain.SwagItem;
import com.swagswap.domain.SwagItemRating;
import com.swagswap.domain.SwagSwapUser;

public class SwagSwapUserServiceImplIntegrationTest extends LocalDatastoreTestCase  {
	
	private UserDaoImpl userDao;
	private SwagSwapUserService swagSwapUserService;
	private ItemDao itemDao;
	private ItemService itemUserService;
	
	@Override
    public void setUp() throws Exception {
        super.setUp();
        if (userDao == null) {
    		UserDaoImpl userDao = new UserDaoImpl();
            userDao.setPersistenceManagerFactory(PMF);
    		this.userDao=userDao;
        }
        if (swagSwapUserService==null) {
        	swagSwapUserService = new SwagSwapUserServiceImpl(userDao);
        }
        if (itemDao == null) {
    		ItemDaoImpl itemDao = new ItemDaoImpl();
            itemDao.setPersistenceManagerFactory(PMF);
    		this.itemDao=itemDao;
        }
        if (itemUserService==null) {
        	itemUserService = new ItemServiceImpl(itemDao);
        }
	}

    public void testFindByEmail() {
        
        SwagSwapUser swagSwapUser = Fixture.createUser();
        userDao.insert(swagSwapUser);

        SwagSwapUser retrievedUser = swagSwapUserService.findByEmail(swagSwapUser.getEmail());
        assertNotNull(retrievedUser);
    }
    
    public void testAddUserRating() {
        //create user
    	SwagSwapUser swagSwapUser = Fixture.createUser();
        userDao.insert(swagSwapUser);
        
        //create item 1
        SwagItem swagItem1 = Fixture.createSwagItem();
        itemDao.insert(swagItem1);
        
        //create item 2
        SwagItem swagItem2 = Fixture.createSwagItem();
        itemDao.insert(swagItem2);
        
        //create rating 1
        SwagItemRating swagItemRating1 = new SwagItemRating(swagItem1.getKey(), 1);
    	swagSwapUserService.addOrUpdateRating(swagSwapUser.getEmail(), swagItemRating1);
    	
    	//create rating 2
    	SwagItemRating swagItemRating2 = new SwagItemRating(swagItem2.getKey(), 2);
    	swagSwapUserService.addOrUpdateRating(swagSwapUser.getEmail(), swagItemRating2);
    	
    	//verify
    	SwagSwapUser user = swagSwapUserService.findByEmail(swagSwapUser.getEmail());
    	assertEquals(user.getSwagItemRatings().size(),2);
    }
    
    public void testUpdateUserRating() {
    	//create user
    	SwagSwapUser swagSwapUser = Fixture.createUser();
    	userDao.insert(swagSwapUser);
    	
    	//create item
    	SwagItem swagItem = Fixture.createSwagItem();
    	itemDao.insert(swagItem);
    	
    	//create rating
    	SwagItemRating originalRating = new SwagItemRating(swagItem.getKey(), 1);
    	swagSwapUserService.addOrUpdateRating(swagSwapUser.getEmail(), originalRating);
    	
    	//update rating
    	SwagItemRating newRating = new SwagItemRating(swagItem.getKey(), 2);
    	swagSwapUserService.addOrUpdateRating(swagSwapUser.getEmail(), newRating);
    	
    	SwagSwapUser user = swagSwapUserService.findByEmail(swagSwapUser.getEmail());
    	assertEquals(user.getSwagItemRatings().size(),1); //should still only be one rating
    	assertEquals(newRating.getUserRating(), user.getSwagItemRatings().iterator().next().getUserRating());
    }
    
}
