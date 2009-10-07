package com.swagswap.web.jsf.bean;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.swagswap.domain.SwagItem;
import com.swagswap.service.ItemService;

@ManagedBean(name = "swagBean")
@ViewScoped
public class SwagBean implements Serializable {

	static final long serialVersionUID = 1L;

	// Inject the swagItemService Spring Bean
	@ManagedProperty(value = "#{swagItemService}")
	ItemService itemService;

	private Collection<SwagItem> swagList;
	private String searchString = "Search";
	private boolean showClear = false;
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

	public boolean isShowClear() {
		return showClear;
	}

	public void setShowClear(boolean showClear) {
		this.showClear = showClear;
	}

}
