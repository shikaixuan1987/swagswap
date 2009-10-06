package com.swagswap.web.springmvc.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.swagswap.domain.SwagItemRating;
import com.swagswap.domain.SwagSwapUser;

public class ShowRatingStarsTag  extends SimpleTagSupport {
	public static final String RATED_IMAGE = "starOn.gif";
	public static final String NOT_RATED_IMAGE = "starOff.gif";
	// tag attributes
	private Integer numberOfStarsToShow;
	//the following are optional (should have userRating or swagSwapUser and swagaItemKey to calculate rating)
	private Integer userRating; //optional
	private SwagSwapUser swagSwapUser; //optional
	private Long swagItemKey; 
	
	/**
	 * Gets user rating and shows appropriate stars
	 */
	public void doTag() throws JspException, IOException {
		Integer userRating = this.userRating;
		if (userRating==null) { //we'll have to look it up
			if (swagSwapUser==null) {
				throw new RuntimeException("tag requires either userRating or swagSwapUser and swagItemKey");
			}
			SwagItemRating swagItemRating = swagSwapUser.getSwagItemRating(swagItemKey);
			if (swagItemRating==null) {
				userRating=0;
			}
			else {
				userRating=swagItemRating.getUserRating();
			}
		}
		String starImage = (userRating.equals(numberOfStarsToShow)) ? RATED_IMAGE : NOT_RATED_IMAGE;
		for (int i=0;i<numberOfStarsToShow;i++) {
			getJspContext().getOut().println("<img src=\"/images/" + starImage + "\" border=\"0\"/>");
		}
	}

	public void setNumberOfStarsToShow(Integer numberOfStarsToShow) {
		this.numberOfStarsToShow = numberOfStarsToShow;
	}
	
	public void setUserRating(Integer userRating) {
		this.userRating = userRating;
	}

	public void setSwagSwapUser(SwagSwapUser swagSwapUser) {
		this.swagSwapUser = swagSwapUser;
	}

	public void setSwagItemKey(Long swagItemKey) {
		this.swagItemKey = swagItemKey;
	}
	
	

}
