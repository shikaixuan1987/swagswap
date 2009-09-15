package com.itcommand.test;

import java.util.ArrayList;
import java.util.List;

import com.itcommand.domain.SwagItem;

public class Fixture {

	public static SwagItem createSwagItem() {
		byte[] imageBytes = new byte[]{0,1,2,3,4,5,6,7,8};
		
		List<String> tags = new ArrayList<String>();
		tags.add("orange");
		tags.add("2008");
		tags.add("squishy");
		
		List<String> comments = new ArrayList<String>();
		comments.add("great");
		comments.add("I want one");
		comments.add("yuck! \n That's terrible");
		
		SwagItem swagItem = new SwagItem(
				"name", 
				"description",
				imageBytes, 
				"owner", 
				5F,
				1,
				tags,
				comments
				);
		return swagItem;
	}
}
