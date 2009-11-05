package com.swagswap.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.swagswap.domain.SwagItem;
import com.swagswap.domain.SwagStats;

public class SwagStatsServiceImpl implements SwagStatsService {

	private static final Logger log = Logger
			.getLogger(SwagStatsServiceImpl.class);

	@Autowired
	private ItemService itemService;

	public SwagStatsServiceImpl() {
		super();
	}

	public SwagStats getSwagStats() {
		SwagStats swagStats = new SwagStats();
		List<SwagItem> swagList = itemService.getAll();
		swagStats.setTopRated(getTopRatedSwag(swagList));
		swagStats.setMostRated(getMostRatedSwag(swagList));
		swagStats.setMostCommented(getMostCommentedSwag(swagList));
		return swagStats;
	}

	private List<SwagItem> getTopRatedSwag(List<SwagItem> swagList) {
		List<SwagItem> topRated = new ArrayList<SwagItem>(swagList);
		Collections.sort(topRated, new AverageRatingComparator());
		return topRated.subList(0, 5);
	}

	private List<SwagItem> getMostRatedSwag(List<SwagItem> swagList) {
		List<SwagItem> mostRated = new ArrayList<SwagItem>(swagList);
		Collections.sort(mostRated, new MostRatedComparator());
		return mostRated.subList(0, 5);
	}

	private List<SwagItem> getMostCommentedSwag(List<SwagItem> swagList) {
		List<SwagItem> mostCommented = new ArrayList<SwagItem>(swagList);
		Collections.sort(mostCommented, new MostCommentedComparator());
		return mostCommented.subList(0, 5);
	}

	private static class AverageRatingComparator implements
			Comparator<SwagItem> {

		public int compare(SwagItem item1, SwagItem item2) {
			if (item1.getAverageRating().equals(item2.getAverageRating())) {
				//  Compare by name if ratings are equal
				return item1.getName().compareToIgnoreCase(item2.getName());
			}
			// Descending
			return item2.getAverageRating().compareTo(item1.getAverageRating());
		}
	}

	private static class MostRatedComparator implements Comparator<SwagItem> {

		public int compare(SwagItem item1, SwagItem item2) {
			if (item1.getNumberOfRatings().equals(item2.getNumberOfRatings())) {
				//  Compare by name if ratings are equal
				return item1.getName().compareToIgnoreCase(item2.getName());
			}
			// Descending
			return item2.getNumberOfRatings().compareTo(
					item1.getNumberOfRatings());
		}
	}

	private static class MostCommentedComparator implements
			Comparator<SwagItem> {

		public int compare(SwagItem item1, SwagItem item2) {
			if (item1.getNumberOfComments().equals(item2.getNumberOfComments())) {
				//  Compare by name if comments are equal
				return item1.getName().compareToIgnoreCase(item2.getName());
			}
			// Descending
			return item2.getNumberOfComments().compareTo(
					item1.getNumberOfComments());
		}
	}
}
