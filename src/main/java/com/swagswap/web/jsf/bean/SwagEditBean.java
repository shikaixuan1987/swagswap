package com.swagswap.web.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.swagswap.domain.SwagItem;
import com.swagswap.web.jsf.model.SwagItemWrapper;

/**
 * @author scott
 * 
 *         View scope bean to store SwagItem being edited or inserted.
 *         Simplifies state saving.
 * 
 */
@ManagedBean(name = "swagEditBean")
@ViewScoped
public class SwagEditBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private SwagItemWrapper editSwagItem;
	private String newComment = "";


	private Long selectedRowId;
	
	public Long getSelectedRowId() {
		return selectedRowId;
	}

	public void setSelectedRowId(Long selectedRowId) {
		this.selectedRowId = selectedRowId;
	}

	public SwagEditBean() {
		super();
		initialiseSwagItem();
	}
	
	public String getNewComment() {
		return newComment;
	}

	public void setNewComment(String newComment) {
		this.newComment = newComment;
	}
	
	public SwagItemWrapper getEditSwagItem() {
		return editSwagItem;
	}

	public void setEditSwagItem(SwagItemWrapper editSwagItem) {
		this.editSwagItem = editSwagItem;
	}

	private void initialiseSwagItem() {
		editSwagItem = new SwagItemWrapper(new SwagItem(),0, true);
		List<String> tagList = new ArrayList<String>();
		for (int i = 0; i < 4; i++) {
			tagList.add("");
		}
		editSwagItem.getSwagItem().setTags(tagList);
	}



}
