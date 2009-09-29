package com.swagswap.web.springmvc.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class LoginLogoutTag  extends SimpleTagSupport {

	// tag attributes
	private String requestURL; //can't get it ourselves cause we can't get the HTTPServletRequest
	
	public void doTag() throws JspException, IOException {
		UserService userService = UserServiceFactory.getUserService();
		if (userService.isUserLoggedIn()) {
			String logoutUrl = userService.createLogoutURL(requestURL);
			getJspContext().getOut().println("<a href=\"" + logoutUrl + "\">logout</a>");
		}
		else {
			String loginUrl = userService.createLoginURL(requestURL);
			getJspContext().getOut().println("<a title=\"your email address will remain private\" href=\"" + loginUrl + "\">login</a>");
		}
	}

	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}
	
	
}
