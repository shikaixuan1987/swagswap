package com.swagswap.web.jsf.bean;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;

import com.swagswap.web.jsf.model.SwagItemWrapper;

@ManagedBean(name = "swagBean")
@ViewScoped
public class SwagBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(SwagBean.class);

	private static final Integer rowsPerPage = 5;
	
	private String page = "1";

	private String searchString = "Search";
	private Boolean showClear = false;
	private Long selectedRowId;
	private List<SwagItemWrapper> swagList;
	private Integer firstRow = 0;

	public void actionClearSearch() {
		showClear = false;
		searchString = "";
		refreshSwagList();
	}
	
	public int getRowsPerPage() {
		return rowsPerPage;
	}

	public void refreshSwagList() {
		// Set swagList to null to force refresh on next request
		swagList = null;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public List<SwagItemWrapper> getSwagList() {
		return swagList;
	}

	public void setSwagList(List<SwagItemWrapper> swagList) {
		this.swagList = swagList;
	}

	public Boolean getShowClear() {
		return showClear;
	}

	public void setShowClear(Boolean showClear) {
		this.showClear = showClear;
	}

	public Integer getFirstRow() {
		return firstRow;
	}

	public void setFirstRow(Integer firstRow) {
		this.firstRow = firstRow;
	}
	
	public int getTableSize() {
		return (swagList == null ? 0 : swagList.size());
	}
	
	public int getLastRow() {
		return (swagList.size() < (firstRow + rowsPerPage)) ? swagList.size() : firstRow + rowsPerPage;
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
		if (swagList != null) {
			Iterator<SwagItemWrapper> iter = swagList.iterator();
			while (iter.hasNext()) {
				SwagItemWrapper item = (SwagItemWrapper) iter.next();
				if (item.getSwagItem().getKey().equals(selectedRowId)) {
					selectedRow = item;
				}
			}
		}
		return selectedRow;
	}
	
	public void actionPage() {
		
		if (page.equals("last")) {
			firstRow = new Double((Math.floor(getTableSize() / rowsPerPage) * rowsPerPage)).intValue();
			return;
		}
		if (page.equals("first")) {
			firstRow = 0;
			return;
		}
		if (page.equals("prev")) {
			firstRow = firstRow - rowsPerPage;
			return;
		}
		if (page.equals("next")) {
			firstRow = firstRow + rowsPerPage;
			return;
		}
		
		int pageInt = Integer.parseInt(page);
		if (pageInt == -1) {
			// last page
			
			return;
		}
		firstRow = pageInt * rowsPerPage - rowsPerPage;
	}
	
	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

}
