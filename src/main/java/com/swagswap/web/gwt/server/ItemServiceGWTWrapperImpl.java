package com.swagswap.web.gwt.server;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.swagswap.domain.SwagItem;
import com.swagswap.domain.SwagItemComment;
import com.swagswap.service.ItemService;
import com.swagswap.web.gwt.client.ItemServiceGWTWrapper;
import com.swagswap.web.gwt.client.domain.SwagItemGWTDTO;

public class ItemServiceGWTWrapperImpl extends AutoinjectingRemoteServiceServlet implements ItemServiceGWTWrapper
	{
	private static final long serialVersionUID = 1L;
	
	private ItemService itemService;
	
	@Autowired
	@Required
	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	public void addComment(SwagItemComment swagItemComment) {
		itemService.addComment(swagItemComment);
	}

	public void delete(Long id) {
		itemService.delete(id);
	}

	public SwagItemGWTDTO get(Long id) {
		return toDTO(itemService.get(id));
	}

	public SwagItemGWTDTO get(Long id, boolean loadSwagImage) {
		return toDTO(itemService.get(id, loadSwagImage));
	}

	public ArrayList<SwagItemGWTDTO> getAll() {
		return toDTOList(itemService.getAll());
	}

	public void save(SwagItemGWTDTO swagItemDto){
		itemService.save(toSwagItem(swagItemDto));
	}

	public ArrayList<SwagItemGWTDTO> search(String queryString) {
		return toDTOList(itemService.search(queryString));
	}

	public void updateRating(Long swagItemKey, int computedRatingDifference,
			boolean isNew) {
		itemService.updateRating(swagItemKey, computedRatingDifference, isNew);
	}
	
	private ArrayList<SwagItemGWTDTO> toDTOList(List<SwagItem> swagItems) {
		ArrayList<SwagItemGWTDTO> dtos = new ArrayList<SwagItemGWTDTO>();
		for (SwagItem swagItem : swagItems) {
			dtos.add(toDTO(swagItem));
		}
		return dtos;
	}
	
	private <T> ArrayList<T> toCopiedArrayList(List<T> listItems) {
		ArrayList<T> copiedList = new ArrayList<T>();
		for (T listItem : listItems) {
			copiedList.add(listItem);
		}
		return copiedList;
	}
	
	/**
	 * For DTO
	 */
	
	public SwagItem toSwagItem (SwagItemGWTDTO dto) {
		SwagItem swagItem = new SwagItem();
		swagItem.setKey(dto.getKey());
		swagItem.setName(dto.getName());
		swagItem.setCompany(dto.getCompany());
		swagItem.setImageBytes(dto.getNewImageBytes());
		swagItem.setImageURL(dto.getNewImageURL());
		swagItem.setOwnerEmail(dto.getOwnerEmail());
		//no need for image,averageRating,numberOfRatings,created,lastUpdated,comments
		swagItem.setTags(dto.getTags());
		return swagItem;
	}

	public SwagItemGWTDTO toDTO(SwagItem swagItem) {
		return new SwagItemGWTDTO(
				swagItem.getKey(),
				swagItem.getName(),
				swagItem.getCompany(),
				swagItem.getDescription(),
				swagItem.getImageKey(),
				swagItem.getOwnerEmail(),
				swagItem.getOwnerNickName(),
				swagItem.getAverageRating(),
				swagItem.getNumberOfRatings(),
				swagItem.getCreated(),
				swagItem.getLastUpdated(),
				toCopiedArrayList(swagItem.getTags()),
				toCopiedArrayList(swagItem.getComments())
				);
	}
}
