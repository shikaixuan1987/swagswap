package com.itcommand.dao;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query;
import com.itcommand.domain.SwagItem;
import com.itcommand.test.Fixture;
import com.itcommand.test.LocalDatastoreTestCase;


public class SwagItemDaoImplTest extends LocalDatastoreTestCase  {

    public void testSave() {
    	SwagItemDaoImpl swagItemDao = new SwagItemDaoImpl();
        swagItemDao.setPersistenceManagerFactory(PMF);
        
        SwagItem originalSwagItem = Fixture.createSwagItem();
        swagItemDao.save(originalSwagItem);

        Query query = new Query(SwagItem.class.getSimpleName());
        assertEquals(1, DatastoreServiceFactory.getDatastoreService().prepare(query).countEntities());
//        assertEquals(originalSwagItem,savedItem);
    }

    public void testDelete() {
    	SwagItemDaoImpl swagItemDao = new SwagItemDaoImpl();
        swagItemDao.setPersistenceManagerFactory(PMF);
        
        SwagItem swagItem = Fixture.createSwagItem();
        swagItemDao.save(swagItem);

        swagItemDao.delete(swagItem.getKey());
        Query query = new Query(SwagItem.class.getSimpleName());
        assertEquals(0, DatastoreServiceFactory.getDatastoreService().prepare(query).countEntities());
    }
}
