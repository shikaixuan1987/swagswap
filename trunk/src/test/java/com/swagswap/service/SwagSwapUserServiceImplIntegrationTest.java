package com.swagswap.service;

import com.swagswap.common.Fixture;
import com.swagswap.common.LocalDatastoreTestCase;
import com.swagswap.dao.ItemDao;
import com.swagswap.dao.ItemDaoImpl;
import com.swagswap.dao.UserDaoImpl;
import com.swagswap.domain.SwagItem;
import com.swagswap.domain.SwagItemRating;
import com.swagswap.domain.SwagSwapUser;
import com.swagswap.exceptions.InvalidSwagItemRatingException;

public class SwagSwapUserServiceImplIntegrationTest extends LocalDatastoreTestCase  {
	
	private UserDaoImpl userDao;
	private SwagSwapUserService swagSwapUserService;
	private ItemDao itemDao;
	private ItemService itemService;
	
	@Override
    public void setUp() throws Exception {
        super.setUp();
        if (userDao == null) {
    		UserDaoImpl userDao = new UserDaoImpl();
            userDao.setPersistenceManagerFactory(PMF);
    		this.userDao=userDao;
        }
        if (itemDao == null) {
        	ItemDaoImpl itemDao = new ItemDaoImpl();
        	itemDao.setPersistenceManagerFactory(PMF);
        	this.itemDao=itemDao;
        }
        if (itemService==null) {
        	itemService = new ItemServiceImpl(itemDao);
        }
        if (swagSwapUserService==null) {
        	//TODO what are we gonna do about Google service null injection?
        	swagSwapUserService = new SwagSwapUserServiceImpl(userDao, itemService, null);
        }
	}

    public void testFindByEmail() {
        
        SwagSwapUser swagSwapUser = Fixture.createUser();
        userDao.insert(swagSwapUser);

        SwagSwapUser retrievedUser = swagSwapUserService.findByEmail(swagSwapUser.getEmail());
        assertNotNull(retrievedUser);
    }
    
    
    public void testFindByEmailOrCreate_non_existent_user() {
    	SwagSwapUser retrievedUser = swagSwapUserService.findByEmailOrCreate("someemail@gmail.com");
    	assertNotNull(retrievedUser);
    }
    
    public void testFindByEmailOrCreate_existing_user() {
    	
    	//Try it with existing user
    	//create user
    	SwagSwapUser swagSwapUser = Fixture.createUser();
    	userDao.insert(swagSwapUser);
    	
    	//verify
    	SwagSwapUser retrievedUser = swagSwapUserService.findByEmailOrCreate(swagSwapUser.getEmail());
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
    
    public void testAddUserRating_non_existent_user () {
    	//don't create user
    	
        //create item
        SwagItem swagItem1 = Fixture.createSwagItem();
        itemDao.insert(swagItem1);
        
        //create rating
        SwagItemRating swagItemRating1 = new SwagItemRating(swagItem1.getKey(), 1);
    	swagSwapUserService.addOrUpdateRating("someemail@gmail.com", swagItemRating1);
    	
    	//verify
    	SwagSwapUser user = swagSwapUserService.findByEmailOrCreate("someemail@gmail.com");
    	assertEquals(user.getSwagItemRatings().size(),1);
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
    	//Gosh this is the only way to get the first Item of a Set in Java
    	assertEquals(newRating.getUserRating(), user.getSwagItemRatings().iterator().next().getUserRating());
    }
    
    public void testAddOrUpdateRating_invalid_rating() {
    	//create user
    	SwagSwapUser swagSwapUser = Fixture.createUser();
    	userDao.insert(swagSwapUser);
    	
    	//create item
    	SwagItem swagItem = Fixture.createSwagItem();
    	itemDao.insert(swagItem);
    	
    	//create INVALID rating
    	SwagItemRating originalRating = new SwagItemRating(swagItem.getKey(), null);
    	try {
    		swagSwapUserService.addOrUpdateRating(swagSwapUser.getEmail(), originalRating);
    		fail ("should have thrown InvalidSwagItemRatingException");
    	}
    	catch (InvalidSwagItemRatingException e) {
    		//good
    	}
    }
    
}
