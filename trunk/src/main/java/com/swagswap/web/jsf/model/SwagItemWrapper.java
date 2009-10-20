package com.swagswap.web.jsf.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.swagswap.domain.SwagItem;
import com.swagswap.web.jsf.bean.UserBean;

/**
 * @author scott
 * 
 *        Wrapper class for SwagItem to make list processing simpler in JSF
 */
public class SwagItemWrapper implements Serializable {

	private static final long serialVersionUID = 1L;

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
	
	public static List<SwagItemWrapper> convertSwagListToWrapperList(
			List<SwagItem> itemList, UserBean userBean) {

		List<SwagItemWrapper> wrapperList = new ArrayList<SwagItemWrapper>();
		Iterator<SwagItem> iter = itemList.iterator();
		while (iter.hasNext()) {
			SwagItem swagItem = iter.next();

			wrapperList.add(new SwagItemWrapper(swagItem,userBean
					.getUserRatingForItem(swagItem)));
		}
		return wrapperList;
	}


	private SwagItem swagItem;
	private Integer userRating;

}
