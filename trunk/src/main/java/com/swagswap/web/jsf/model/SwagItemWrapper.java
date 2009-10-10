package com.swagswap.web.jsf.model;

import com.swagswap.domain.SwagItem;

/**
 * @author scott
 * 
 *        Wrapper class for SwagItem to make list processing simpler in JSF
 */
public class SwagItemWrapper {

	public SwagItemWrapper(SwagItem swagItem, Integer userRating) {
		super();
		this.swagItem = swagItem;
		this.userRating = userRating;
	}

	public SwagItem getSwagItem() {
		return swagItem;
	}

	public void setSwagItem(SwagItem swagItem) {
		this.swagItem = swagItem;
	}

	public Integer getUserRating() {
		return userRating;
	}

	public void setUserRating(Integer userRating) {
		this.userRating = userRating;
	}

	private SwagItem swagItem;
	private Integer userRating;

}
