package com.swagswap.web.gwt.server;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.swagswap.domain.SwagItem;
import com.swagswap.domain.SwagItemComment;
import com.swagswap.service.ItemService;

public class ItemServiceGWTWrapperImpl extends AutoinjectingRemoteServiceServlet
										implements ItemService {
	private static final long serialVersionUID = 1L;
	
	private ItemService itemService;
	
	@Autowired
	@Required
	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	@Override
	public void addComment(SwagItemComment swagItemComment) {
		itemService.addComment(swagItemComment);
	}

	@Override
	public void delete(Long id) {
		itemService.delete(id);
	}

	@Override
	public SwagItem get(Long id) {
		return itemService.get(id);
	}

	@Override
	public SwagItem get(Long id, boolean loadSwagImage) {
		return itemService.get(id, loadSwagImage);
	}

	@Override
	public List<SwagItem> getAll() {
		return itemService.getAll();
	}

	@Override
	public void save(SwagItem swagItem){
		itemService.save(swagItem);
	}

	@Override
	public Collection<SwagItem> search(String queryString) {
		return itemService.search(queryString);
	}

	@Override
	public void updateRating(Long swagItemKey, int computedRatingDifference,
			boolean isNew) {
		itemService.updateRating(swagItemKey, computedRatingDifference, isNew);
	}
}
