package com.swagswap.web.jsf.bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import com.swagswap.domain.SwagItem;
import com.swagswap.service.ItemService;

@ManagedBean(name = "swagEditBean")
@SessionScoped
public class SwagEditBean {

	// Inject the swagItemService Spring Bean
	@ManagedProperty(value = "#{swagItemService}")
	ItemService itemService;
	private SwagItem editSwagItem = new SwagItem();
	private Long selectedRowId;
	
	public SwagEditBean() {
		super();
		initialiseSwagItem();
	}

	private void initialiseSwagItem() {
		editSwagItem = new SwagItem();
		List<String> tagList = new ArrayList<String>();
		for (int i = 0; i < 4; i++) {
			tagList.add("");
		}
		editSwagItem.setTags(tagList);
	}

	public Long getSelectedRowId() {
		return selectedRowId;
	}

	public void setSelectedRowId(Long selectedRowId) {
		this.selectedRowId = selectedRowId;
	}

	public String actionSaveItem() {
		getItemService().save(editSwagItem);
		initialiseSwagItem();

		return "allSwag?faces-redirect=true";
	}
	
	public String actionAddItem() {
		initialiseSwagItem();
		return "addSwag?faces-redirect=true";
	}

	public String actionEditItem() {
		// Refresh selected item from DB
		editSwagItem = getItemService().get(getSelectedRowId());
		return "editSwag?faces-redirect=true";
	}

	public SwagItem getEditSwagItem() {
		return editSwagItem;
	}

	public void setEditSwagItem(SwagItem editSwagItem) {
		this.editSwagItem = editSwagItem;
	}

	public ItemService getItemService() {
		return itemService;
	}

	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

}
