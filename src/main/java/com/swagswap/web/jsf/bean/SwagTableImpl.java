package com.swagswap.web.jsf.bean;

import java.io.Serializable;
import java.util.List;

import com.swagswap.web.jsf.model.SwagItemWrapper;

public class SwagTableImpl implements Serializable, SwagTable {

	private static final long serialVersionUID = 1L;

	private static final Integer rowsPerPage = 10;
	
	private List<SwagItemWrapper> swagList;
	private Integer firstRow = 0;
	
	private String page = "1";
	
	public SwagTableImpl() {
		super();
	}
	
	public SwagTableImpl(List<SwagItemWrapper> swagList) {
		this();
		this.swagList = swagList;
	}
	
	/* (non-Javadoc)
	 * @see com.swagswap.web.jsf.bean.SwagTable#getPage()
	 */
	public String getPage() {
		return page;
	}

	/* (non-Javadoc)
	 * @see com.swagswap.web.jsf.bean.SwagTable#setPage(java.lang.String)
	 */
	public void setPage(String page) {
		this.page = page;
	}

	/* (non-Javadoc)
	 * @see com.swagswap.web.jsf.bean.SwagTable#getSwagList()
	 */
	public List<SwagItemWrapper> getSwagList() {
		return swagList;
	}

	/* (non-Javadoc)
	 * @see com.swagswap.web.jsf.bean.SwagTable#setSwagList(java.util.List)
	 */
	public void setSwagList(List<SwagItemWrapper> swagList) {
		this.swagList = swagList;
	}

	/* (non-Javadoc)
	 * @see com.swagswap.web.jsf.bean.SwagTable#getFirstRow()
	 */
	public Integer getFirstRow() {
		return firstRow;
	}

	/* (non-Javadoc)
	 * @see com.swagswap.web.jsf.bean.SwagTable#setFirstRow(java.lang.Integer)
	 */
	public void setFirstRow(Integer firstRow) {
		this.firstRow = firstRow;
	}
	
	/* (non-Javadoc)
	 * @see com.swagswap.web.jsf.bean.SwagTable#getRowsPerPage()
	 */
	public int getRowsPerPage() {
		return rowsPerPage;
	}
	
	/* (non-Javadoc)
	 * @see com.swagswap.web.jsf.bean.SwagTable#getTableSize()
	 */
	public int getTableSize() {
		return (swagList == null ? 0 : swagList
				.size());
	}

	/* (non-Javadoc)
	 * @see com.swagswap.web.jsf.bean.SwagTable#getLastRow()
	 */
	public int getLastRow() {
		return (swagList.size() < (firstRow + rowsPerPage)) ? swagList.size() : firstRow + rowsPerPage;
	}
	
	/* (non-Javadoc)
	 * @see com.swagswap.web.jsf.bean.SwagTable#actionPage()
	 */
	public void actionPage() {
		// TODO. Refactor this. Scott.
		if (page.equals("last")) {
			firstRow = new Double(
					(Math.floor(getTableSize() / rowsPerPage) * rowsPerPage))
					.intValue();
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


	
}
