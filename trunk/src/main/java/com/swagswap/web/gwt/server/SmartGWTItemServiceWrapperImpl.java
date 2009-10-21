package com.swagswap.web.gwt.server;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.swagswap.domain.SwagItem;
import com.swagswap.domain.SwagItemComment;
import com.swagswap.service.ItemService;
import com.swagswap.web.gwt.client.SmartGWTItemServiceWrapper;
import com.swagswap.web.gwt.client.domain.SwagItemGWTDTO;

/**
 * Wraps ItemService to apply the DTO (anti)pattern.  Necessary since annotated
 * JDO objects (with GAE specific types like Text and Blob) won't serialize for GWT RPC 
 */
public class SmartGWTItemServiceWrapperImpl extends
		AutoinjectingRemoteServiceServlet implements SmartGWTItemServiceWrapper {
	private static final long serialVersionUID = 1L;

	private ItemService itemService;

	@Autowired
	@Required
	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	public List<SwagItemGWTDTO> fetch() {
		return toDTOList(itemService.getAll());
	}

	//SmartGWT requires the updated item to be returned
	public SwagItemGWTDTO add(SwagItemGWTDTO swagItemGWTDTO) {
		SwagItem updatedItem = itemService.save(toSwagItem(swagItemGWTDTO));
		return toDTO(updatedItem);
	}

	// TOOD combine these if possible
	public SwagItemGWTDTO update(SwagItemGWTDTO swagItemGWTDTO) {
		return add(swagItemGWTDTO);
	}

	public void remove(SwagItemGWTDTO swagItemGWTDTO) {
		itemService.delete(swagItemGWTDTO.getKey());
	}

	public void save(SwagItemGWTDTO swagItemDto){
		itemService.save(toSwagItem(swagItemDto));
	}

	public void updateRating(Long swagItemKey, int computedRatingDifference,
			boolean isNew) {
		itemService.updateRating(swagItemKey, computedRatingDifference, isNew);
	}
	
	public void addComment(SwagItemComment swagItemComment) {
		itemService.addComment(swagItemComment);
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

	public SwagItem toSwagItem(SwagItemGWTDTO dto) {
		SwagItem swagItem = new SwagItem();
		swagItem.setKey(dto.getKey());
		swagItem.setName(dto.getName());
		swagItem.setCompany(dto.getCompany());
		swagItem.setImageKey(dto.getImageKey());
		swagItem.setImageBytes(dto.getNewImageBytes());
		swagItem.setImageURL(dto.getNewImageURL());
		swagItem.setOwnerEmail(dto.getOwnerEmail());
		// no need for
		// image,averageRating,numberOfRatings,created,lastUpdated,comments
		swagItem.setTags(dto.getTags());
		return swagItem;
	}

	public SwagItemGWTDTO toDTO(SwagItem swagItem) {
		return new SwagItemGWTDTO(swagItem.getKey(), swagItem.getName(),
				swagItem.getCompany(), swagItem.getDescription(), swagItem.getImageKey(), swagItem
						.getOwnerEmail(), swagItem.getOwnerNickName(), swagItem
						.getAverageRating(), swagItem.getNumberOfRatings(),
				swagItem.getCreated(), swagItem.getLastUpdated(),
				toCopiedArrayList(swagItem.getTags()),
				toCopiedArrayList(swagItem.getComments()));
	}
}