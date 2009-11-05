package com.swagswap.domain;

import java.io.Serializable;
import java.util.List;

public class SwagStats implements Serializable {

	private static final long serialVersionUID = 1L;

	List<SwagItem> topRated;
	List<SwagItem> mostRated;
	List<SwagItem> mostCommented;
	List<SwagSwapUserStats> userCreated;
	List<SwagSwapUserStats> userRated;
	List<SwagSwapUserStats> userCommented;
	Integer totalItems = 0;
	Integer totalUsers = 0;
	Integer totalComments = 0;
	Integer totalRatings = 0;
	
	public Integer getTotalComments() {
		return totalComments;
	}

	public void setTotalComments(Integer totalComments) {
		this.totalComments = totalComments;
	}

	public Integer getTotalRatings() {
		return totalRatings;
	}

	public void setTotalRatings(Integer totalRatings) {
		this.totalRatings = totalRatings;
	}
	
	public List<SwagSwapUserStats> getUserCommented() {
		return userCommented;
	}

	public void setUserCommented(List<SwagSwapUserStats> userCommented) {
		this.userCommented = userCommented;
	}
	public List<SwagSwapUserStats> getUserRated() {
		return userRated;
	}

	public void setUserRated(List<SwagSwapUserStats> userRated) {
		this.userRated = userRated;
	}


	public Integer getTotalUsers() {
		return totalUsers;
	}

	public void setTotalUsers(Integer totalUsers) {
		this.totalUsers = totalUsers;
	}

	public Integer getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(Integer totalItems) {
		this.totalItems = totalItems;
	}

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

	public List<SwagSwapUserStats> getUserCreated() {
		return userCreated;
	}

	public void setUserCreated(List<SwagSwapUserStats> userCreated) {
		this.userCreated = userCreated;
	}

}
