package com.swagswap.web.jsf.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.apache.log4j.Logger;

import com.swagswap.domain.SwagItem;
import com.swagswap.domain.SwagItemComment;
import com.swagswap.domain.SwagItemRating;
import com.swagswap.domain.SwagSwapUser;
import com.swagswap.service.ItemService;
import com.swagswap.service.SwagSwapUserService;
import com.swagswap.web.jsf.model.SwagItemWrapper;

@ManagedBean(name = "actionBean")
@RequestScoped
public class ActionBean {

	private static final Logger log = Logger.getLogger(ActionBean.class);
	
	// TODO  Inject SwagImpl

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

	public void actionAddComment() throws IOException {

		Long key = swagEditBean.getEditSwagItem().getSwagItem().getKey();
		String newComment = swagEditBean.getNewComment();

		if (newComment.trim().length() == 0) {
			return;
		}

		SwagItemComment comment = new SwagItemComment(key, swagSwapUserService
				.getCurrentUser().getNickname(), newComment);
		itemService.addComment(comment);
		swagEditBean.setNewComment("");
	}

	public void populateSwagItem() {
		Long key = swagEditBean.getSelectedRowId();
		SwagItem item = itemService.get(key);
		// TODO. Take this out and test
		hackSwagItemList(item);

		// TODO Why should I need to do this? Image shouldn't be loaded into
		// Item.
		item.setImage(null);

		swagEditBean.setEditSwagItem(new SwagItemWrapper(item, userBean
				.getUserRatingForItem(key, userBean.getLoggedInUser()),
				userBean.isItemOwner(item)));
	}

	public void populateSwagList() {
		SwagTable swagTable = swagBean.getSwagTable();
		if (swagTable == null) {
			swagTable = new SwagTableImpl();
		}
		if (swagTable.getSwagList() == null) {
			List<SwagItem> swagList = itemService.getAll();
			swagTable.setSwagList((SwagItemWrapper
					.convertSwagListToWrapperList(swagList, userBean)));
			swagBean.setSwagTable(swagTable);
			swagBean.setTotalItems(swagList.size());
		}
	}
	
	public void populateMySwagList() {
		// TODO Refactor
		SwagTable swagTable = swagBean.getSwagTable();
		if (swagTable == null) {
			swagTable = new SwagTableImpl();
		}
		if (swagTable.getSwagList() == null) {
			long start = new Date().getTime();
			List<SwagItem> swagList = itemService.getAll();
			swagTable.setSwagList(SwagItemWrapper
					.convertSwagListToWrapperList(swagList, userBean));

			// All swag
			swagBean.setTotalItems(swagList.size());
			
			// Created
			SwagTable createdTable = new SwagTableImpl((SwagItemWrapper
					.convertSwagListToWrapperList(itemService
							.filterByOwnerNickName(swagList,
									swagSwapUserService.getCurrentUser()
											.getNickname()), userBean)));
			swagBean.setCreatedTable(createdTable);

			// Commented
			SwagTable commentedTable = new SwagTableImpl(
					(SwagItemWrapper.convertSwagListToWrapperList(itemService
							.filterByCommentedOn(swagList, swagSwapUserService
									.getCurrentUser().getNickname()), userBean)));
			swagBean.setCommentedTable(commentedTable);

			// Rated
			SwagSwapUser user = swagSwapUserService.findByEmail();
			SwagTable ratedTable = new SwagTableImpl((SwagItemWrapper
					.convertSwagListToWrapperList(itemService.filterByRated(
							swagList, user),
							userBean)));
			swagBean.setRatedTable(ratedTable);
			
			swagBean.setFilter("CREATE");
		}
	}

	public void actionDelete() {
		itemService.delete(swagBean.getSelectedRowId());
		swagBean.refreshSwagList();
	}

	public void actionRateSwag() throws IOException {
		// TODO temporary hack until I figure out how ot pass action to Stars
		// component
		if ((SwagItemWrapper) swagBean.getSelectedRow() == null) {
			rate(swagEditBean.getEditSwagItem());
			return;
		}
		actionRateSwagFromTable();
		//  TODO.  If rated table not null then ensure rated item is refreshed.
		if (swagBean.getRatedTable() != null) {
			System.out.println("****  Rated table is not null");
			if (swagBean.getRatedTable().getSwagList().contains(swagBean.getSelectedRow())) {
				System.out.println("****  Rated table contains rated item");
			}
		}
	}

	public void actionRateSwagFromTable() throws IOException {
		rate((SwagItemWrapper) swagBean.getSelectedRow());
	}

	public void actionSearch() {
		String searchString = swagBean.getSearchString();
		if (searchString.trim().length() < 1) {
			return;
		}
		swagBean.getSwagTable().setSwagList(
				SwagItemWrapper.convertSwagListToWrapperList(itemService
						.search(searchString), userBean));
		swagBean.setShowClear(true);
	}

	private void rate(SwagItemWrapper ratedItem) throws IOException {

		long startTime = new Date().getTime();

		if (!swagSwapUserService.isUserLoggedIn()) {
			userBean.showLogin();
			return;
		}

		// Service call to update rating
		swagSwapUserService.addOrUpdateRating(swagSwapUserService
				.getCurrentUser().getEmail(), new SwagItemRating(ratedItem
				.getSwagItem().getKey(), ratedItem.getUserRating()));
		// Get item from service for recalculated average rating
		ratedItem
				.setSwagItem(itemService.get(ratedItem.getSwagItem().getKey()));
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
