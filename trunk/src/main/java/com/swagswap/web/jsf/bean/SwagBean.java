package com.swagswap.web.jsf.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import com.swagswap.domain.SwagItem;
import com.swagswap.service.ItemService;
import com.swagswap.web.jsf.model.SwagItemWrapper;

@ManagedBean(name = "swagBean")
@RequestScoped
public class SwagBean {

	// Inject the swagItemService Spring Bean
	@ManagedProperty(value = "#{swagItemService}")
	private ItemService itemService;

	@ManagedProperty(value = "#{swagEditBean}")
	private SwagEditBean swagEditBean;
	
	@ManagedProperty(value = "#{userBean}")
	private UserBean userBean;



	private Collection<SwagItemWrapper> swagList;
	private String searchString = "Search";
	private Boolean showClear = false;
	private Long selectedRowId;

	public void populateSwagItem() {
		SwagItem item = getItemService().get(getSelectedRowId());
		hackSwagItemList(item);
		setEditSwagItem(item);
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
		if (searchString.length() < 1) {
			return;
		}
		swagList = convertSwagListToWrapperList(getItemService().search(searchString));
		showClear = true;
	}

	public void actionClearSearch() {
		swagList = null;
		showClear = false;
		searchString = "";
	}

	public String actionSaveItem() {
		getItemService().save(getEditSwagItem());
		return "allSwag?faces-redirect=true";
	}

	public void actionDelete() {
		getItemService().delete(getSelectedRowId());
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

	public Collection<SwagItemWrapper> getSwagList() {
		if (swagList == null) {
			swagList = convertSwagListToWrapperList(getItemService().getAll());
		}
		return swagList;
	}

	private Collection<SwagItemWrapper> convertSwagListToWrapperList(
			Collection<SwagItem> itemList) {

		List<SwagItemWrapper> wrapperList = new ArrayList<SwagItemWrapper>();
		Iterator<SwagItem> iter = itemList.iterator();
		while (iter.hasNext()) {
			SwagItem swagItem = (SwagItem) iter.next();
			Integer userItemRating;
			if (getUserBean().getLoggedInUser().getSwagItemRating(swagItem.getKey()) == null) {
				userItemRating = 0;
			} else {
				userItemRating = getUserBean().getLoggedInUser().getSwagItemRating(swagItem.getKey()).getUserRating();
			}
			wrapperList.add(new SwagItemWrapper(swagItem, userItemRating));	
		}

		return wrapperList;
	}

	public void setSwagList(Collection<SwagItemWrapper> swagList) {
		this.swagList = swagList;
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

	public SwagItem getEditSwagItem() {
		return getSwagEditBean().getEditSwagItem();
	}

	public void setEditSwagItem(SwagItem editSwagItem) {
		getSwagEditBean().setEditSwagItem(editSwagItem);
	}

}
