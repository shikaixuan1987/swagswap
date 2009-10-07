package com.swagswap.web.jsf.bean;

import java.util.Collection;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import com.swagswap.domain.SwagItem;
import com.swagswap.service.ItemService;

@ManagedBean(name = "swagBean")
@RequestScoped
public class SwagBean {

	static final long serialVersionUID = 1L;

	// Inject the swagItemService Spring Bean
	@ManagedProperty(value = "#{swagItemService}")
	private ItemService itemService;

	private Collection<SwagItem> swagList;
	private String searchString = "Search";
	private Boolean showClear = false;
	private Long selectedRowId;

	public Long getSelectedRowId() {
		return selectedRowId;
	}

	public void setSelectedRowId(Long selectedRowId) {
		this.selectedRowId = selectedRowId;
	}

	public void actionSearch() {
		if (searchString.length() < 1) {
			return;
		}
		swagList = getItemService().search(searchString);
		showClear = true;
	}

	public void actionClearSearch() {
		swagList = null;
		showClear = false;
		searchString = "";
	}

	public void actionDelete() {
		getItemService().delete(getSelectedRowId());
		swagList = getItemService().getAll();
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

	public Collection<SwagItem> getSwagList() {
		if (swagList == null) {
			swagList = getItemService().getAll();
		}
		return swagList;
	}

	public void setSwagList(Collection<SwagItem> swagList) {
		this.swagList = swagList;
	}

	public Boolean getShowClear() {
		return showClear;
	}

	public void setShowClear(Boolean showClear) {
		this.showClear = showClear;
	}

}
