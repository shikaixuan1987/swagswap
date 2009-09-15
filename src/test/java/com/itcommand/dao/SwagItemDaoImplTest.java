package com.itcommand.dao;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query;
import com.itcommand.domain.SwagItem;
import com.itcommand.test.Fixture;
import com.itcommand.test.LocalDatastoreTestCase;


public class SwagItemDaoImplTest extends LocalDatastoreTestCase  {

    public void testSave() {
        SwagItemDao swagItemDao = new SwagItemDaoImpl();
        SwagItem swagItem = Fixture.createSwagItem();
        swagItemDao.save(swagItem);

        Query query = new Query(SwagItem.class.getSimpleName());
        assertEquals(2, DatastoreServiceFactory.getDatastoreService().prepare(query).countEntities());
    }

    public void testDelete() {
    	SwagItemDao swagItemDao = new SwagItemDaoImpl();
        SwagItem swagItem = Fixture.createSwagItem();
        swagItemDao.save(swagItem);

        swagItemDao.delete(swagItem.getKey());
        Query query = new Query(SwagItem.class.getSimpleName());
        assertEquals(0, DatastoreServiceFactory.getDatastoreService().prepare(query).countEntities());
    }
}
