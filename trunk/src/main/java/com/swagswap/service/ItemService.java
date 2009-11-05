package com.swagswap.service;

import java.util.List;

import com.swagswap.domain.SwagItem;
import com.swagswap.domain.SwagItemComment;
import com.swagswap.domain.SwagSwapUser;
import com.swagswap.exceptions.ImageTooLargeException;
import com.swagswap.exceptions.LoadImageFromURLException;

public interface ItemService {

	SwagItem get(Long id);

	SwagItem get(Long id, boolean loadSwagImage);

	List<SwagItem> search(String queryString);

	List<SwagItem> getAll();

	List<SwagItem> findByTag(String searchString);

	List<SwagItem> filterByOwnerGoogleID(List<SwagItem> swagList,
			String userNickName);

	List<SwagItem> filterByCommentedOn(List<SwagItem> swagList,
			String userNickName, boolean exclusive);

	List<SwagItem> filterByRated(List<SwagItem> swagList, SwagSwapUser user,
			boolean exclusive);

	SwagItem save(SwagItem swagItem) throws LoadImageFromURLException;

	void updateRating(Long swagItemKey, int computedRatingDifference,
			boolean isNew);

	void delete(Long id);

	void addComment(SwagItemComment swagItemComment);

	byte[] getImageDataFromURL(String imageURL)
			throws LoadImageFromURLException, ImageTooLargeException;

}
