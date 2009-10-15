package com.swagswap.web.jsf.bean;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		return swagSwapUserService.getCurrentUser().getEmail();
	}

	public String getUserName() {
		return swagSwapUserService.getCurrentUser().getEmail();
	}

	public SwagSwapUser getLoggedInUser() {
		return swagSwapUserService.findByEmailOrCreate((getUserEmail()));
	}

	private String getCurrentURL() {
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		return request.getRequestURL().toString();
	}

	/**
	 * Hack to redirect to Google generated login page. <h:outputLink> was
	 * encoding part of the URL and Google wouldn't accept that
	 * 
	 * @throws IOException
	 */
	public void showLogin() throws IOException {
		HttpServletResponse response = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();
		response.sendRedirect(swagSwapUserService
				.createLoginURL(getCurrentURL()));
	}

	/**
	 * Hack to redirect to Google generated logout page. <h:outputLink> was
	 * encoding part of the URL and Google wouldn't accept that
	 * 
	 * @throws IOException
	 */
	public void showLogout() throws IOException {
		HttpServletResponse response = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();
		response.sendRedirect(swagSwapUserService
				.createLogoutURL(getCurrentURL()));
	}

	public Integer getUserRatingForItem(SwagItem swagItem) {
		// TODO Maybe service should default user rating to zero if it
		// doesn't exist
		if (! isLoggedIn()) {
			return 0;
		}
		
		Integer userItemRating;
		if (getLoggedInUser().getSwagItemRating(swagItem.getKey()) == null) {
			userItemRating = 0;
		} else {
			userItemRating = getLoggedInUser().getSwagItemRating(
					swagItem.getKey()).getUserRating();
		}
		// Temporary hack
		if (userItemRating == null) {
			userItemRating = 0;
		}
		return userItemRating;
	}

}
