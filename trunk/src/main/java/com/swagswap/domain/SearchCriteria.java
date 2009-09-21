package com.swagswap.domain;

public class SearchCriteria {
	private String searchString;

	public SearchCriteria() {
	}

	public SearchCriteria(String searchString) {
		this.searchString=searchString;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
}
