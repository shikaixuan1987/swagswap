package com.swagswap.web.jsf.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import com.swagswap.domain.SwagItem;
import com.swagswap.domain.SwagItemComment;
import com.swagswap.domain.SwagItemRating;
import com.swagswap.service.ItemService;
import com.swagswap.service.SwagSwapUserService;
import com.swagswap.web.jsf.model.SwagItemWrapper;

@ManagedBean(name = "swagBean")
@RequestScoped
public class SwagBean {

	// Inject the swagItemService Spring Bean
	@ManagedProperty(value = "#{swagItemService}")
	private ItemService itemService;

	// Inject the swagSwapUserService Spring Bean
	@ManagedProperty(value = "#{swagSwapUserService}")
	private SwagSwapUserService swagSwapUserService;

	@ManagedProperty(value = "#{swagEditBean}")
	private SwagEditBean swagEditBean;

	@ManagedProperty(value = "#{userBean}")
	private UserBean userBean;

	// private Collection<SwagItemWrapper> swagList;
	private String searchString = "Search";
	private Boolean showClear = false;
	private Long selectedRowId;
	private Integer userRating;

	public Integer getUserRating() {
		return userRating;
	}

	public void setUserRating(Integer userRating) {
		this.userRating = userRating;
	}

	public SwagSwapUserService getSwagSwapUserService() {
		return swagSwapUserService;
	}

	public void setSwagSwapUserService(SwagSwapUserService swagSwapUserService) {
		this.swagSwapUserService = swagSwapUserService;
	}

	public void populateSwagItem() {
		// TODO This is called each time the view page is rendered. This isn't
		// desirable. Refactor.
		if (getSelectedRowId() != null) {
			SwagItem item = getItemService().get(getSelectedRowId());
			hackSwagItemList(item);
			getSwagEditBean().setEditSwagItem(item);
			getSwagEditBean().setUserRating(
					getUserBean().getUserRatingForItem(item));
		}
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
				for (int i = 0; i < 4; i++) {
					if (item.getTags().get(i) == null) {
						item.getTags().set(i, "");
					}
				}

			}
		}
	}

	public void actionSearch() {
		if (searchString.trim().length() < 1) {
			return;
		}
		swagEditBean.setSwagList(SwagItemWrapper.convertSwagListToWrapperList(
				getItemService().search(searchString), userBean));
		showClear = true;
	}

	public void actionClearSearch() {
		showClear = false;
		searchString = "";
		refreshSwagList();
	}

	public String actionSaveItem() {
		getItemService().save(getSwagEditBean().getEditSwagItem());
		return "allSwag?faces-redirect=true";
	}

	public void actionDelete() {
		getItemService().delete(getSelectedRowId());
		refreshSwagList();
	}

	private void refreshSwagList() {
		// Set swagList to null to force refresh on next get
		swagEditBean.setSwagList(null);
	}

	public void actionRateSwag() throws IOException {

		if (!getUserBean().isLoggedIn()) {
			getUserBean().showLogin();
			return;
		}

		swagSwapUserService.addOrUpdateRating(userBean.getUserEmail(),
				new SwagItemRating(getSelectedRowId(), getUserRating()));

		if (swagEditBean.getSwagList() == null) {
			// Updating from view item so no list to refresh
			return;
		}
		// Performance enhancement. Only get the updated row from service and
		// refresh list.
		// TODO Do we need this with caching?
		SwagItem refreshedSwagItem = itemService.get(getSelectedRowId());
		Integer newRating = getUserRating();
		SwagItemWrapper refreshedItem = new SwagItemWrapper(refreshedSwagItem,
				newRating);

		ListIterator<SwagItemWrapper> iter = swagEditBean.getSwagList()
				.listIterator();
		while (iter.hasNext()) {
			SwagItemWrapper item = (SwagItemWrapper) iter.next();
			if (item.getSwagItem().getKey().equals(getSelectedRowId())) {
				iter.set(refreshedItem);
				break;
			}

		}
	}

	public void actionAddComment() {
		String newComment = swagEditBean.getNewComment();
		// TODO Change to Client Validator
		if (newComment.trim().length() == 0) {
			return;
		}
		Long itemKey = swagEditBean.getEditSwagItem().getKey();
		SwagItemComment comment = new SwagItemComment(itemKey, userBean
				.getUserName(), newComment);
		itemService.addComment(comment);
		swagEditBean.setNewComment("");
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public ItemService getItemService() {
		return itemService;
	}

	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	public SwagEditBean getSwagEditBean() {
		return swagEditBean;
	}

	public void setSwagEditBean(SwagEditBean swagEditBean) {
		this.swagEditBean = swagEditBean;
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public List<SwagItemWrapper> getSwagList() {
		if (swagEditBean.getSwagList() == null) {
			swagEditBean.setSwagList(SwagItemWrapper
					.convertSwagListToWrapperList(getItemService().getAll(),
							userBean));
		}

		return swagEditBean.getSwagList();
	}

	public void setSwagList(List<SwagItemWrapper> swagList) {
		swagEditBean.setSwagList(swagList);
	}

	public Boolean getShowClear() {
		return showClear;
	}

	public void setShowClear(Boolean showClear) {
		this.showClear = showClear;
	}

	public Long getSelectedRowId() {
		return selectedRowId;
	}

	public void setSelectedRowId(Long selectedRowId) {
		this.selectedRowId = selectedRowId;
	}

}
