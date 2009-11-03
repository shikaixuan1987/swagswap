package com.swagswap.web.jsf.bean;

import java.io.Serializable;
import java.util.Iterator;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;

import com.swagswap.web.jsf.model.SwagItemWrapper;

@ManagedBean(name = "swagBean")
@ViewScoped
public class SwagBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(SwagBean.class);

	private String searchString = "Search";
	private Boolean showClear = false;
	private Long selectedRowId;
	private String filter = "";
	private Integer totalItems = 0;
	private Integer createdItems = 0;
	private Integer ratedItems = 0;
	private Integer commentedItems = 0;

	// Used for View all
	private SwagTable swagTable;
	// Used for MySwag
	private SwagTable createdTable;
	private SwagTable commentedTable;
	private SwagTable ratedTable;
	private SwagTable notCommentedTable;
	private SwagTable notRatedTable;

	public SwagTable getNotRatedTable() {
		return notRatedTable;
	}

	public void setNotRatedTable(SwagTable notRatedTable) {
		this.notRatedTable = notRatedTable;
	}

	public SwagTable getNotCommentedTable() {
		return notCommentedTable;
	}

	public void setNotCommentedTable(SwagTable notCommentedTable) {
		this.notCommentedTable = notCommentedTable;
	}

	public Integer getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(Integer totalItems) {
		this.totalItems = totalItems;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public SwagTable getCreatedTable() {
		return createdTable;
	}

	public void setCreatedTable(SwagTable createdTable) {
		this.createdTable = createdTable;
	}

	public SwagTable getCommentedTable() {
		return commentedTable;
	}

	public void setCommentedTable(SwagTable commentedTable) {
		this.commentedTable = commentedTable;
	}

	public SwagTable getRatedTable() {
		return ratedTable;
	}

	public void setRatedTable(SwagTable ratedTable) {
		this.ratedTable = ratedTable;
	}

	public Integer getCreatedItems() {
		return createdItems;
	}

	public void setCreatedItems(Integer createdItems) {
		this.createdItems = createdItems;
	}

	public Integer getCommentedItems() {
		return commentedItems;
	}

	public void setCommentedItems(Integer commentedItems) {
		this.commentedItems = commentedItems;
	}

	public Integer getRatedItems() {
		return ratedItems;
	}

	public void setRatedItems(Integer ratedItems) {
		this.ratedItems = ratedItems;
	}

	public SwagTable getSwagTable() {

		if (filter.equals("CREATE")) {
			return createdTable;
		}
		if (filter.equals("RATE")) {
			return ratedTable;
		}
		if (filter.equals("NOTRATE")) {
			return notRatedTable;
		}
		if (filter.equals("COMMENT")) {
			return commentedTable;
		}
		if (filter.equals("NOTCOMMENT")) {
			return notCommentedTable;
		}
		return swagTable;
	}

	public void setSwagTable(SwagTable swagTable) {
		this.swagTable = swagTable;
	}

	public void actionClearSearch() {
		showClear = false;
		searchString = "";
		refreshSwagList();
	}

	public void refreshSwagList() {
		// Set swagList to null to force refresh on next request
		getSwagTable().setSwagList(null);
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
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

	public SwagItemWrapper getSelectedRow() {
		// Can't use component binding on dataTable as it's not Serializable
		SwagItemWrapper selectedRow = null;
		if (getSwagTable() != null) {
			Iterator<SwagItemWrapper> iter = getSwagTable().getSwagList()
					.iterator();
			while (iter.hasNext()) {
				SwagItemWrapper item = (SwagItemWrapper) iter.next();
				if (item.getSwagItem().getKey().equals(selectedRowId)) {
					selectedRow = item;
				}
			}
		}
		return selectedRow;
	}

	// TODO Refactor this colour stuff! Quick and dirty. Consider storing
	// percentages...

	public String getCreatedColour() {

		return getColourForPercentage(getCreatedTable().getTableSize());
	}

	public String getRatedColour() {

		return getColourForPercentage(getRatedTable().getTableSize());
	}

	public String getCommentedColour() {

		return getColourForPercentage(getCommentedTable().getTableSize());
	}

	private String getColourForPercentage(int tableSize) {

		if (totalItems == 0) {
			return "white";
		}
		float percent = tableSize / totalItems.floatValue();

		if (percent == 1) {
			return "#FF8800";
		}
		if (percent < 0.33) {
			return "red";
		}
		if (percent < 0.66) {
			return "yellow";
		}
		if (percent < 1) {
			return "lime";
		}

		return "white";

	}

}
