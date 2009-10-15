package com.swagswap.web.gwt.server;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import net.sf.gilead.adapter4appengine.EngineRemoteService;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Expose Spring services to GWT app
 * User Gilead adapter4appengine to help GWT serialize appengine domain objects
 * From http://pgt.de/2009/07/17/non-invasive-gwt-and-spring-integration-reloaded/
 */
public abstract class AutoinjectingRemoteServiceServlet extends EngineRemoteService {

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		WebApplicationContext ctx = WebApplicationContextUtils
				.getRequiredWebApplicationContext(config.getServletContext());
		AutowireCapableBeanFactory beanFactory = ctx
				.getAutowireCapableBeanFactory();
		beanFactory.autowireBean(this);
	}

}
