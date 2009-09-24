package com.swagswap.web.springmvc.controller;

import junit.framework.TestCase;

import com.swagswap.dao.SwagImageDaoImpl;
import com.swagswap.domain.SwagImage;
import com.swagswap.web.springmvc.controller.SwagImageController;

public class SwagImageControllerTest extends TestCase {

	public void testConstructDefaultImageURL() {
		String requestURL = "http://localhost:8080/swag/showImage/blahblah";
		SwagImageController swagImageController 
			= new SwagImageController(new SwagImageDaoImpl());
		String defaultImageURL = swagImageController.constructDefaultImageURL(requestURL);
		assertEquals("http://localhost:8080/" + SwagImageController.PATH_TO_DEFAULT_IMAGE,
				      defaultImageURL);
	}
}
