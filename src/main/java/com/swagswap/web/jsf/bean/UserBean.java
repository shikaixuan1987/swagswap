package com.swagswap.web.jsf.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.swagswap.domain.SwagSwapUser;
import com.swagswap.service.SwagSwapUserService;

@ManagedBean(name = "userBean")
@RequestScoped
public class UserBean {

	// Inject the swagSwapUserService Spring Bean
	@ManagedProperty(value = "#{swagSwapUserService}")
	private SwagSwapUserService swagSwapUserService;

	public SwagSwapUserService getSwagSwapUserService() {
		return swagSwapUserService;
	}

	public void setSwagSwapUserService(SwagSwapUserService swagSwapUserService) {
		this.swagSwapUserService = swagSwapUserService;
	}

	public boolean isLoggedIn() {
		return swagSwapUserService.isUserLoggedIn();
	}

	public String getUserName() {
		return swagSwapUserService.getCurrentUser().getEmail();
	}

	public String getLoginURL() {
		return swagSwapUserService.createLoginURL(getCurrentUrl());
	}
	
	public String getLogoutURL() {
		return swagSwapUserService.createLogoutURL(getCurrentUrl());
	}
	
	public SwagSwapUser getLoggedInUser() {
		return swagSwapUserService.findByEmail(getUserName());
	}

	private String getCurrentUrl() {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		return request.getRequestURI();
	}

}
