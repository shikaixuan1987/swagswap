package com.swagswap.web.springmvc.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Checks to see if the logged in user is allowed to access the current item
 * and includes the body of the tag if they are
 *
 */
public class IsAllowedTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(IsAllowedTag.class);
	// tag attributes
	private String swagItemOwnerEmail;

	public int doStartTag() throws JspException {
		UserService userService = UserServiceFactory.getUserService();
		boolean isLoggedIn = userService.isUserLoggedIn();
		boolean isAdmin=false;
		boolean emailMatchesCurrentItemOwner=false;
		if (isLoggedIn) {
			isAdmin = userService.isUserAdmin();
			emailMatchesCurrentItemOwner = userService.getCurrentUser().getEmail().equals(swagItemOwnerEmail);
		}
		if (isLoggedIn && (isAdmin || emailMatchesCurrentItemOwner)) {
			return EVAL_BODY_INCLUDE;
		}
		else {
			return SKIP_BODY;
		}
	}

	public void setSwagItemOwnerEmail(String swagItemOwnerEmail) {
		this.swagItemOwnerEmail = swagItemOwnerEmail;
	}
}