package com.swagswap.web.jsf.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.apache.log4j.Logger;

import com.swagswap.domain.SwagItem;
import com.swagswap.domain.SwagItemComment;
import com.swagswap.domain.SwagItemRating;
import com.swagswap.service.ItemService;
import com.swagswap.service.SwagSwapUserService;
import com.swagswap.web.jsf.model.SwagItemWrapper;

@ManagedBean(name = "actionBean")
@RequestScoped
public class ActionBean {

	private static final Logger log = Logger.getLogger(ActionBean.class);

	// Inject the swagItemService Spring Bean
	@ManagedProperty(value = "#{swagItemService}")
	private ItemService itemService;

	// Inject the swagSwapUserService Spring Bean
	@ManagedProperty(value = "#{swagSwapUserService}")
	private SwagSwapUserService swagSwapUserService;

	@ManagedProperty(value = "#{userBean}")
	private UserBean userBean;

	@ManagedProperty(value = "#{swagEditBean}")
	private SwagEditBean swagEditBean;

	@ManagedProperty(value = "#{swagBean}")
	private SwagBean swagBean;

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	public SwagSwapUserService getSwagSwapUserService() {
		return swagSwapUserService;
	}

	public void setSwagSwapUserService(SwagSwapUserService swagSwapUserService) {
		this.swagSwapUserService = swagSwapUserService;
	}

	public String actionSaveItem() {
		itemService.save(swagEditBean.getEditSwagItem().getSwagItem());
		return "allSwag?faces-redirect=true";
	}

	public void actionAddComment() {
		Long key = swagEditBean.getEditSwagItem().getSwagItem().getKey();
		String newComment = swagEditBean.getNewComment();

		if (newComment.trim().length() == 0) {
			return;
		}

		SwagItemComment comment = new SwagItemComment(key, userBean
				.getUserName(), newComment);
		itemService.addComment(comment);
		swagEditBean.setNewComment("");
	}

	public void populateSwagItem() {
		Long key = swagEditBean.getSelectedRowId();
		hackSwagItemList(swagEditBean.getEditSwagItem().getSwagItem());
		swagEditBean.setEditSwagItem(new SwagItemWrapper(itemService.get(key),
				userBean.getUserRatingForItem(key)));
	}

	public void populateSwagList() {
		System.out.println("****  Populate SwagList");
		List<SwagItemWrapper> swagList = swagBean.getSwagList();
		if (swagList == null) {
			swagBean.setSwagList((SwagItemWrapper.convertSwagListToWrapperList(
					itemService.getAll(), userBean)));
		}
		

	}

	public void actionDelete() {
		itemService.delete(swagBean.getSelectedRowId());
		swagBean.refreshSwagList();
	}

	public void actionRateSwag() throws IOException {
		System.out.println("****  ActionBean.  Action Rate Swag "+swagEditBean.getEditSwagItem());
		//  TODO  temporary hack until I figure out how ot pass action to Stars component
		if ((SwagItemWrapper) swagBean.getSelectedRow() == null) {
			System.out.println("****  ActionBean.  Action Rate Swag.  Table value is null. Rating from View ");
			rate(swagEditBean.getEditSwagItem());
			return;
		}
		System.out.println("****  ActionBean.  Action Rate Swag.  Table value is not null. Rating from Table ");
		actionRateSwagFromTable();
	}

	public void actionRateSwagFromTable() throws IOException {
		rate((SwagItemWrapper) swagBean.getSelectedRow());
	}

	public void actionSearch() {
		String searchString = swagBean.getSearchString();
		if (searchString.trim().length() < 1) {
			return;
		}
		swagBean.setSwagList(SwagItemWrapper.convertSwagListToWrapperList(
				itemService.search(searchString), userBean));
		swagBean.setShowClear(true);
	}

	private void rate(SwagItemWrapper ratedItem) throws IOException {

		long startTime = new Date().getTime();

		if (!userBean.isLoggedIn()) {
			userBean.showLogin();
			return;
		}

		// Service call to update rating
		swagSwapUserService.addOrUpdateRating(userBean.getUserEmail(),
				new SwagItemRating(ratedItem.getSwagItem().getKey(), ratedItem
						.getUserRating()));
		// Get item from service for recalculated average rating
		ratedItem
				.setSwagItem(itemService.get(ratedItem.getSwagItem().getKey()));

		log.error("****  Elapsed time caching is "
				+ ((new Date().getTime()) - startTime));

	}

	public void setSwagEditBean(SwagEditBean swagEditBean) {
		this.swagEditBean = swagEditBean;
	}

	public void setSwagBean(SwagBean swagBean) {
		this.swagBean = swagBean;
	}

	private void hackSwagItemList(SwagItem item) {
		// hack Item to add empty Strings to List
		List<String> tagList = new ArrayList<String>();
		if (item != null) {
			if (item.getTags() == null) {
				for (int i = 0; i < 4; i++) {
					tagList.add("");
				}
				item.setTags(tagList);
			} else {
				for (int i = item.getTags().size(); i < 4; i++) {
					item.getTags().add(i, "");
				}

			}
		}
	}

}
