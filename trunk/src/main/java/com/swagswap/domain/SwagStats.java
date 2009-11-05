package com.swagswap.domain;

import java.io.Serializable;
import java.util.List;

public class SwagStats implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public List<SwagItem> getTopRated() {
		return topRated;
	}
	public void setTopRated(List<SwagItem> topRated) {
		this.topRated = topRated;
	}
	public List<SwagItem> getMostRated() {
		return mostRated;
	}
	public void setMostRated(List<SwagItem> mostRated) {
		this.mostRated = mostRated;
	}
	public List<SwagItem> getMostCommented() {
		return mostCommented;
	}
	public void setMostCommented(List<SwagItem> mostCommented) {
		this.mostCommented = mostCommented;
	}
	List<SwagItem> topRated;
	List<SwagItem> mostRated;
	List<SwagItem> mostCommented;

}
