package com.swagswap.web.springmvc.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Checks to see if the user is not logged in
 * and includes the body of the tag if they are
 *
 */
public class IsNotLoggedInTag extends TagSupport {
	private static final long serialVersionUID = 1L;

	public int doStartTag() throws JspException {
		UserService userService = UserServiceFactory.getUserService();
		boolean isLoggedIn = userService.isUserLoggedIn();
		if (!isLoggedIn) {
			return EVAL_BODY_INCLUDE;
		}
		else {
			return SKIP_BODY;
		}
	}
}