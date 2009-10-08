package com.swagswap.service;

import com.swagswap.common.Fixture;
import com.swagswap.common.LocalDatastoreTestCase;
import com.swagswap.domain.SwagItem;
import com.swagswap.exceptions.ImageTooLargeException;
import com.swagswap.exceptions.InvalidSwagItemException;

public class ItemServiceImplTest extends LocalDatastoreTestCase  {
	
	private ItemServiceImpl itemService;
	
	@Override
    public void setUp() throws Exception {
        super.setUp();
        if (itemService==null) {
        	itemService = new ItemServiceImpl();
        }
	}

    public void testpopulateSwagImageFromURL() {
        
        SwagItem swagItem = Fixture.createSwagItem();
        swagItem.setImageURL("http://www.google.com/images/nav_logo7.png"); //google logo
        itemService.populateSwagImageFromURL(swagItem);
        
        assertNotNull(swagItem.getImage());
    }
    
    public void testpopulateSwagImageFromURL_image_too_large() {
    	
    	SwagItem swagItem = Fixture.createSwagItem();
    	swagItem.setImageURL("http://www.vs.inf.ethz.ch/contact/pics/tram-map-large.gif"); //big image
    	try {
    		itemService.populateSwagImageFromURL(swagItem);
    		fail ("Should have thrown an ImageTooLargeException");
    	} catch (ImageTooLargeException e) {
    		//good
    	}
    	
    	assertNotNull(swagItem.getImage());
    }

    public void testSave_with_no_swagItem_name() {
    	SwagItem swagItem = Fixture.createSwagItem();
    	swagItem.setName("");
    		try {
    			itemService.save(swagItem);
    			fail ("should have thrown InvalidSwagItemException");
    		}
    		catch (InvalidSwagItemException e) {
    			//good
    		}
    }
}
