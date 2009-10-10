package com.swagswap.web.jsf.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.users.UserService;
import com.swagswap.domain.SwagSwapUser;
import com.swagswap.service.SwagSwapUserService;

@ManagedBean(name = "userBean")
@RequestScoped
public class UserBean {

	// Inject the googleUserService Spring Bean
	@ManagedProperty(value = "#{googleUserService}")
	private UserService googleUserService;

	// Inject the swagSwapUserService Spring Bean
	@ManagedProperty(value = "#{swagSwapUserService}")
	private SwagSwapUserService swagSwapUserService;

	public UserService getGoogleUserService() {
		return googleUserService;
	}

	public void setGoogleUserService(UserService googleUserService) {
		this.googleUserService = googleUserService;
	}

	public SwagSwapUserService getSwagSwapUserService() {
		return swagSwapUserService;
	}

	public void setSwagSwapUserService(SwagSwapUserService swagSwapUserService) {
		this.swagSwapUserService = swagSwapUserService;
	}

	public boolean isLoggedIn() {
		return getGoogleUserService().isUserLoggedIn();
	}

	public String getUserName() {
		return getGoogleUserService().getCurrentUser().getEmail();
	}

	public String getLoginURL() {
		return getGoogleUserService().createLoginURL(getCurrentUrl());
	}
	
	public String getLogoutURL() {
		return getGoogleUserService().createLogoutURL(getCurrentUrl());
	}
	
	public SwagSwapUser getLoggedInUser() {
		return getSwagSwapUserService().findByEmail(getUserName());
	}

	private String getCurrentUrl() {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		return request.getRequestURI();
	}

}
