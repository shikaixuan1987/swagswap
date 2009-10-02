package com.swagswap.web.springmvc.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class ShowRatingStarsTag  extends SimpleTagSupport {
	public static final String RATED_IMAGE = "starOn.gif";
	public static final String NOT_RATED_IMAGE = "starOff.gif";
	// tag attributes
	private Integer numberOfStarsToShow;
	private Integer userRating; //optional
	
	public void doTag() throws JspException, IOException {
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

}
