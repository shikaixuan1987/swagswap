package com.swagswap.web.springmvc.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class LoginLogoutTag  extends SimpleTagSupport {

	// tag attributes
	private String requestURL; //can't get it ourselves cause we can't get the HTTPServletRequest
	//These are all optional with default values
	private boolean showLogout=true; 
	private String loginText="login";
	private String loginTooltipText="your email address will remain private";
	private String logoutText="logout";
	
	public void doTag() throws JspException, IOException {
		UserService userService = UserServiceFactory.getUserService();
		if (userService.isUserLoggedIn() && showLogout) {
			String logoutUrl = userService.createLogoutURL(requestURL);
			getJspContext().getOut().println("<a href=\"" + logoutUrl + "\">"+ logoutText + "</a>");
			getJspContext().getOut().println("&nbsp;[Welcome: " + userService.getCurrentUser().getNickname() + "]");
		}
		else {
			String loginUrl = userService.createLoginURL(requestURL);
			getJspContext().getOut().println("<a title=\""+ loginTooltipText + "\" href=\"" + loginUrl + "\">"+ loginText + "</a>");
		}
	}

	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}

	public void setShowLogout(boolean showLogout) {
		this.showLogout = showLogout;
	}

	public void setLoginText(String loginText) {
		this.loginText = loginText;
	}

	public void setLoginTooltipText(String loginTooltipText) {
		this.loginTooltipText = loginTooltipText;
	}

	public void setLogoutText(String logoutText) {
		this.logoutText = logoutText;
	}
}
