package com.swagswap.web.springmvc.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Checks to see if the user is admin
 * and includes the body of the tag if they are
 *
 */
public class IsAdminTag extends TagSupport {
	private static final long serialVersionUID = 1L;

	public int doStartTag() throws JspException {
		UserService userService = UserServiceFactory.getUserService();
		boolean isAdmin = userService.isUserLoggedIn() && userService.isUserAdmin();
		if (isAdmin) {
			return EVAL_BODY_INCLUDE;
		}
		else {
			return SKIP_BODY;
		}
	}
}