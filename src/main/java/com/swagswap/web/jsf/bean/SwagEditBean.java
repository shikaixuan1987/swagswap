package com.swagswap.web.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.swagswap.domain.SwagItem;
import com.swagswap.domain.SwagItemComment;

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
	private SortedSet<SwagItemComment> comments;
	private String newComment = "";

	public String getNewComment() {
		return newComment;
	}

	public void setNewComment(String newComment) {
		this.newComment = newComment;
	}

	public SortedSet<SwagItemComment> getComments() {
		return comments;
	}

	public void setComments(SortedSet<SwagItemComment> comments) {
		this.comments = comments;
	}

	public Date getTime() {
		return new Date();
	}

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
