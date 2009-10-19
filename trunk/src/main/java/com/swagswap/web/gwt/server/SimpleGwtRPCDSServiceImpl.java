package com.swagswap.web.gwt.server;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.swagswap.domain.SwagItem;
import com.swagswap.service.ItemService;
import com.swagswap.web.gwt.client.SimpleGwtRPCDSService;
import com.swagswap.web.gwt.client.domain.SwagItemGWTDTO;

/**
 * 
 * @author Aleksandras Novikovas
 * @author System Tier
 * @version 1.0
 */
public class SimpleGwtRPCDSServiceImpl extends
		AutoinjectingRemoteServiceServlet implements SimpleGwtRPCDSService {
	private static final long serialVersionUID = 1L;

	private ItemService itemService;

	// static List<SwagItemGWTDTO> list;
	//
	// static int id;
	// static {
	// id = 1;
	// list = new ArrayList<SwagItemGWTDTO> ();
	// SwagItemGWTDTO swagItemGWTDTO;
	// swagItemGWTDTO = new SwagItemGWTDTO ();
	// swagItemGWTDTO.setId (id++);
	// swagItemGWTDTO.setPicture("picture" + id);
	// swagItemGWTDTO.setName ("First");
	// swagItemGWTDTO.setDate (new Date ());
	// list.add (swagItemGWTDTO);
	// swagItemGWTDTO = new SwagItemGWTDTO ();
	// swagItemGWTDTO.setId (id++);
	// swagItemGWTDTO.setPicture("picture" + id);
	// swagItemGWTDTO.setName ("Second");
	// swagItemGWTDTO.setDate (new Date ());
	// list.add (swagItemGWTDTO);
	// swagItemGWTDTO = new SwagItemGWTDTO ();
	// swagItemGWTDTO.setId (id++);
	// swagItemGWTDTO.setPicture("picture" + id);
	// swagItemGWTDTO.setName ("Third");
	// swagItemGWTDTO.setDate (new Date ());
	// list.add (swagItemGWTDTO);
	// }

	@Autowired
	@Required
	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	public List<SwagItemGWTDTO> fetch() {
		return toDTOList(itemService.getAll());
	}

	// public SwagItemGWTDTO add (SwagItemGWTDTO swagItemGWTDTO) {
	public void add(SwagItemGWTDTO swagItemGWTDTO) {
		itemService.save(toSwagItem(swagItemGWTDTO));
	}

	// TOOD combine these if possible
	public void update(SwagItemGWTDTO swagItemGWTDTO) {
		add(swagItemGWTDTO);
	}

	public void remove(SwagItemGWTDTO swagItemGWTDTO) {
		itemService.delete(swagItemGWTDTO.getKey());
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
