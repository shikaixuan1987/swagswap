package com.swagswap.web.gwt.server;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.smartgwt.client.widgets.tile.TileRecord;
import com.swagswap.domain.SwagItemRating;
import com.swagswap.domain.SwagSwapUser;
import com.swagswap.exceptions.InvalidSwagItemRatingException;
import com.swagswap.service.SwagSwapUserService;
import com.swagswap.web.gwt.client.LoginService;
import com.swagswap.web.gwt.client.domain.LoginInfo;
import com.swagswap.web.gwt.client.domain.SwagItemGWTDTO;

/**
 * Wraps what we need from SwagSwapUserService. Returns a LoginInfo objects
 * which is needed by GWT since it does an async call onModuleLoad and holds
 * this LoginInfo object for subsequent use. LoginInfo wraps SwagswapUser
 * 
 * @author sam
 * 
 */
public class LoginServiceImpl extends AutoinjectingRemoteServiceServlet
		implements LoginService {

	// HttpSession key
	private static final String CURRENT_TILE_RECORD = "currentTileRecord";
	private SwagSwapUserService swagSwapUserService;

	@Autowired
	@Required
	public void SwagSwapUserService(SwagSwapUserService swagSwapUserService) {
		this.swagSwapUserService = swagSwapUserService;
	}

	public LoginInfo login(String requestUri) {
		LoginInfo loginInfo = new LoginInfo();

		if (swagSwapUserService.isUserLoggedIn()) {
			loginInfo.setLoggedIn(true);
			loginInfo.setLogoutUrl(swagSwapUserService
					.createLogoutURL(requestUri));
			SwagSwapUser user = swagSwapUserService
					.findByEmailOrCreate(swagSwapUserService.getCurrentUser().getEmail());
			// Need a DTO for SwagSwapUser because you can't use JDO attached
			// Set or you get Exception
			// Type 'org.datanucleus.sco.backed.Set' was not included in the set
			// of types which can be serialized by this SerializationPolicy or
			// its Class object could not be loaded. For security purposes, this
			// type will not be serialized
			
			//hosted mode not setting email correctly so use nickName instead
			loginInfo.setEmail(user.getEmail());
			
			loginInfo.setNickName(user.getNickName());
			Set<SwagItemRating> swagItemRatings = user.getSwagItemRatings();
			loginInfo.setSwagItemRatings(toCopiedSet(swagItemRatings));
			//set currentSwagItem
			loginInfo.setCurrentSwagItem((SwagItemGWTDTO)getThreadLocalRequest().getSession().getAttribute(CURRENT_TILE_RECORD));
		} else {
			loginInfo.setLoggedIn(false);
			loginInfo.setLoginUrl(swagSwapUserService
					.createLoginURL(requestUri));
		}
		return loginInfo;
	}

	public void addOrUpdateRating(String userEmail,
			SwagItemRating swagItemRating)
			throws InvalidSwagItemRatingException {
		swagSwapUserService.addOrUpdateRating(userEmail, swagItemRating);
	}
	
	@Override
	public void setCurrentTileRecord(SwagItemGWTDTO dto) {
		getThreadLocalRequest().getSession().setAttribute(CURRENT_TILE_RECORD, dto);
	}

	// for creating a JDO detatched Set
	private Set<SwagItemRating> toCopiedSet(Set<SwagItemRating> setItems) {
		HashSet<SwagItemRating> copiedSet = new HashSet<SwagItemRating>();
		for (SwagItemRating setItem : setItems) {
			copiedSet.add(new SwagItemRating(setItem.getSwagItemKey(), setItem
					.getUserRating()));
		}
		return copiedSet;
	}


}