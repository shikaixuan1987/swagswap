package com.swagswap.dao;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query;
import com.swagswap.common.Fixture;
import com.swagswap.common.LocalDatastoreTestCase;
import com.swagswap.domain.User;

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

    public void testSaveNew() {
        
        User user = Fixture.createUser();
        userDao.save(user);

        assertNumberOfUsers(1);   
        assertNotNull(user.getJoined()); //joined should be set
    }
    
    public void testUpdate() {
    	
    	User orig = Fixture.createUser();
    	userDao.save(orig);
    	
    	orig.setNickName("testie");
    	orig.setRatedSwagItems(Fixture.getRatedItems());
    	userDao.save(orig);
    	
    	User retrievedUser = userDao.get(orig.getKey());
    	assertEquals(orig, retrievedUser);
    }

    public void testFindByEmail() {
    	
        User orig = Fixture.createUser();
        userDao.save(orig);
        
        User retrievedItem = userDao.findByEmail(orig.getEmail());
        assertEquals(orig,retrievedItem);
    }
    
    //Make sure it's null (and doesn't thrown an exception)
    public void testFindByEmail_nonexistant_email() {
    	User retrievedItem = userDao.findByEmail("bogus");
    	assertNull(retrievedItem);
    		
    }
    
    /**
     * Database count assertions
     * @param usersExpected
     */
	private void assertNumberOfUsers(int usersExpected) {
		Query query = new Query(User.class.getSimpleName());
    	assertEquals(usersExpected, DatastoreServiceFactory.getDatastoreService().prepare(query).countEntities());
	}
}
