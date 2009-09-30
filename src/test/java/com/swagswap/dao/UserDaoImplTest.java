package com.swagswap.dao;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query;
import com.swagswap.common.Fixture;
import com.swagswap.common.LocalDatastoreTestCase;
import com.swagswap.domain.SwagSwapUser;

public class UserDaoImplTest extends LocalDatastoreTestCase  {
	
	private UserDaoImpl userDao;
	
	@Override
    public void setUp() throws Exception {
        super.setUp();
        if (userDao == null) {
    		UserDaoImpl userDao = new UserDaoImpl();
            userDao.setPersistenceManagerFactory(PMF);
    		this.userDao=userDao;
        }
	}

    public void testInsert() {
        
        SwagSwapUser swagSwapUser = Fixture.createUser();
        userDao.insert(swagSwapUser);

        assertNumberOfUsers(1);   
        assertNotNull(swagSwapUser.getJoined()); //joined should be set
    }
    
    public void testUpdate() {
    	
    	SwagSwapUser orig = Fixture.createUser();
    	userDao.insert(orig);
    	
    	orig.setNickName("testie");
    	orig.setRatedSwagItems(Fixture.getRatedItems());
    	userDao.update(orig);
    	
    	SwagSwapUser retrievedUser = userDao.get(orig.getKey());
    	assertEquals(orig, retrievedUser);
    }

    public void testFindByEmail() {
    	
        SwagSwapUser orig = Fixture.createUser();
        userDao.insert(orig);
        
        SwagSwapUser retrievedItem = userDao.findByEmail(orig.getEmail());
        assertEquals(orig,retrievedItem);
    }
    
    //Make sure it's null (and doesn't thrown an exception)
    public void testFindByEmail_nonexistant_email() {
    	SwagSwapUser retrievedItem = userDao.findByEmail("bogus");
    	assertNull(retrievedItem);
    		
    }
    
    /**
     * Database count assertions
     * @param usersExpected
     */
	private void assertNumberOfUsers(int usersExpected) {
		Query query = new Query(SwagSwapUser.class.getSimpleName());
    	assertEquals(usersExpected, DatastoreServiceFactory.getDatastoreService().prepare(query).countEntities());
	}
}
