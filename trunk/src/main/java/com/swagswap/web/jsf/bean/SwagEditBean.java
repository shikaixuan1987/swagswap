package com.swagswap.web.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.swagswap.domain.SwagItem;

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

	private Integer userRating = 0;

	private static final long serialVersionUID = 1L;
	private SwagItem editSwagItem;

	public SwagEditBean() {
		super();
		initialiseSwagItem();
	}

	public SwagItem getEditSwagItem() {
		return editSwagItem;
	}

	public void setEditSwagItem(SwagItem editSwagItem) {
		this.editSwagItem = editSwagItem;
	}

	private void initialiseSwagItem() {
		editSwagItem = new SwagItem();
		List<String> tagList = new ArrayList<String>();
		for (int i = 0; i < 4; i++) {
			tagList.add("");
		}
		editSwagItem.setTags(tagList);
	}

	public Integer getUserRating() {
		return userRating;
	}

	public void setUserRating(Integer userRating) {
		this.userRating = userRating;
	}

}
