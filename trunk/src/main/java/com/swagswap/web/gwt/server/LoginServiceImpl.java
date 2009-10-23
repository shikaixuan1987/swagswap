package com.swagswap.web.gwt.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.google.appengine.api.users.User;
import com.swagswap.service.SwagSwapUserService;
import com.swagswap.web.gwt.client.LoginService;
import com.swagswap.web.gwt.client.domain.LoginInfo;

public class LoginServiceImpl extends AutoinjectingRemoteServiceServlet
		implements LoginService {

	private SwagSwapUserService swagSwapUserService;

	@Autowired
	@Required
	public void setItemService(SwagSwapUserService swagSwapUserService) {
		this.swagSwapUserService = swagSwapUserService;
	}

	public LoginInfo login(String requestUri) {
		LoginInfo loginInfo = new LoginInfo();

		if (swagSwapUserService.isUserLoggedIn()) {
			User user = swagSwapUserService.getCurrentUser();
			loginInfo.setLoggedIn(true);
			loginInfo.setEmailAddress(user.getEmail());
			loginInfo.setNickname(user.getNickname());
			loginInfo.setLogoutUrl(swagSwapUserService.createLogoutURL(requestUri));
		} else {
			loginInfo.setLoggedIn(false);
			loginInfo.setLoginUrl(swagSwapUserService.createLoginURL(requestUri));
		}
		return loginInfo;
	}

}
