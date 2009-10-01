package com.swagswap.web.springmvc.controller;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import com.swagswap.domain.SearchCriteria;
import com.swagswap.domain.SwagItem;
import com.swagswap.service.ItemService;

@Controller
public class ItemController {
	private static final Logger log = Logger.getLogger(ItemController.class);

	@Autowired
	private ItemService itemService;
	@Autowired
	private com.google.appengine.api.users.UserService googleUserService;
	
	//TODO do we need request here?
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addHandler(Model model, HttpServletRequest req) {
		model.addAttribute("swagItem", new SwagItem());
		return "addEditSwagItem";
	}

	@RequestMapping(value = "/edit/{key}", method = RequestMethod.GET)
	public String editHandler(@PathVariable("key") Long key, Model model) {
		SwagItem swagItem = itemService.get(key, true);
		model.addAttribute("swagItem", swagItem);
		return "addEditSwagItem";
	}
	
	@RequestMapping(value = "/view/{key}", method = RequestMethod.GET)
	public String viewHandler(@PathVariable("key") Long key, Model model) {
		SwagItem swagItem = itemService.get(key, true);
		model.addAttribute("swagItem", swagItem);
		return "viewRateSwagItem";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveHandler(@ModelAttribute SwagItem swagItem) {
		itemService.save(swagItem);
		return "redirect:/swag/search";
	}
	
	@RequestMapping(value = "/delete/{key}", method = RequestMethod.GET)
	public String deleteHandler(@PathVariable("key") Long key) {
		itemService.delete(key);
		return "redirect:/swag/search";
	}
	
	//For legacy URL that some tweets had already linked to.
	@RequestMapping(value = "/listSwagItems", method = RequestMethod.GET)
	public String listHandler(Model model) {
		return "redirect:/swag/search";
	}

	/**
	 * Searching with no searchString does a listAll
	 * @param searchCriteria
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String searchHandler(@ModelAttribute SearchCriteria searchCriteria, Model model) {
		if (searchCriteria==null) {
			model.addAttribute("searchCriteria", new SearchCriteria());
		}
		Collection<SwagItem> swagItems = itemService.search(searchCriteria.getSearchString());
		model.addAttribute("swagItems", swagItems);
		return "listSwagItems";
	}

	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));

		binder.registerCustomEditor(List.class, new CustomCollectionEditor(List.class));

		// for spring in gae?
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));

		binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
		binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor(false));
		binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, true));
		
		//for image upload
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
	}

	public void setSwagItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	public void setGoogleUserService(com.google.appengine.api.users.UserService googleUserService) {
		this.googleUserService = googleUserService;
	}
	
}