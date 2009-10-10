package com.swagswap.web.jsf.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.users.UserService;
import com.swagswap.domain.SwagItem;
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

	public String getUserEmail() {
		return getGoogleUserService().getCurrentUser().getEmail();
	}
	
	public String getUserName() {
		return getGoogleUserService().getCurrentUser().getNickname();
	}

	public String getLoginURL() {
		return swagSwapUserService.createLoginURL(getCurrentUrl());
	}
	
	public String getLogoutURL() {
		return swagSwapUserService.createLogoutURL(getCurrentUrl());
	}
	
	public SwagSwapUser getLoggedInUser() {
		return getSwagSwapUserService().findByEmailOrCreate((getUserEmail()));
	}

	private String getCurrentUrl() {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		return request.getRequestURI();
	}
	
	public Integer getUserRatingForItem(SwagItem swagItem) {
		// TODO Maybe service should default user rating to zero if it
		// doesn't exist
		Integer userItemRating;
		if (getLoggedInUser().getSwagItemRating(
				swagItem.getKey()) == null) {
			userItemRating = 0;
		} else {
			userItemRating = getLoggedInUser()
					.getSwagItemRating(swagItem.getKey()).getUserRating();
		}
		//  Temporary hack
		if (userItemRating == null) {
			userItemRating = 0;
		}
		return userItemRating;
	}

}
