package com.swagswap.common;

import java.util.ArrayList;
import java.util.List;

import com.swagswap.domain.SwagImage;
import com.swagswap.domain.SwagItem;

public class Fixture {

	public static SwagItem createSwagItem() {
		SwagImage image = new SwagImage(new byte[]{0,1,2,3,4,5,6,7,8});
		
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
				image, 
				"owner", 
				5F,
				1,
				tags,
				comments
				);
		return swagItem;
	}
	
	public static String get510Chars() {
		return "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Duis scelerisque facilisis " +
				"tortor. Aliquam semper gravida ligula. Vestibulum sodales lacinia leo. In turpis justo," +
				" placerat eu, ultrices vel, auctor a, tortor. Aliquam lorem justo, tepor at, vehicula ac," +
				" porttitor id, felis. Ut eget orci at odio congue nonummy. Aliquam wisi. Vivamus suscipit" +
				" nonummy eros. Praesent bibendum consectetuer neque. Donec vitae eros ut mi adipiscing" +
				" mollis. Vestibulum eleifend. Maecenas ultrice. Ut vitae velit. 1 2 3 4 5";
	}

}
