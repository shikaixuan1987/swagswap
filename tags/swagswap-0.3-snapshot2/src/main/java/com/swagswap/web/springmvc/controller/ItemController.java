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
import com.swagswap.domain.SwagItemRating;
import com.swagswap.domain.SwagSwapUser;
import com.swagswap.service.ItemService;
import com.swagswap.service.SwagSwapUserService;

@Controller
public class ItemController {
	private static final Logger log = Logger.getLogger(ItemController.class);

	@Autowired
	private ItemService itemService;
	@Autowired
	private SwagSwapUserService swagSwapUserService;
	@Autowired
	private com.google.appengine.api.users.UserService googleUserService;
	
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
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveHandler(@ModelAttribute SwagItem swagItem) {
		itemService.save(swagItem);
		return "redirect:/swag/search";
	}
	
	@RequestMapping(value = "/view/{key}", method = RequestMethod.GET)
	public String viewHandler(@PathVariable("key") Long key, Model model) {
		SwagItem swagItem = itemService.get(key, true);
		//put rating (if there is one) into the model
		String ratingString = "";
		if (googleUserService.isUserLoggedIn()) {
			//get swagSwapUser using email key from available google user
			//we've got to create them here if they don't already exist in our DB
			SwagSwapUser swagSwapUser = swagSwapUserService.findByEmailOrCreate(
											googleUserService.getCurrentUser().getEmail());
			//see if the already have a rating for this item
			SwagItemRating rating = swagSwapUser.getSwagItemRating(key);
			if (null!=rating) {
				ratingString = rating.getUserRating().toString();
			}
		}
		//make previous rating available to the page to show the right number of stars
		model.addAttribute("userRating", ratingString);
		//backing object for rating form
		model.addAttribute("newRating", new SwagItemRating(swagItem.getKey())); 
		model.addAttribute("swagItem", swagItem);
		return "viewRateSwagItem";
	}
	
	@RequestMapping(value = "/delete/{key}", method = RequestMethod.GET)
	public String deleteHandler(@PathVariable("key") Long key) {
		itemService.delete(key);
		return "redirect:/swag/search";
	}
	
    @RequestMapping(value = "/rate", method = RequestMethod.GET)
	public String rateHandler(@ModelAttribute SwagItemRating swagItemRating, HttpServletRequest request) {
		String email = googleUserService.getCurrentUser().getEmail();
		swagSwapUserService.addOrUpdateRating(email, swagItemRating);
		String referer = request.getHeader("Referer");
		if (referer==null) { //in case browser doesn't support Redirect header
			referer="/swag/search";
		}
		return "redirect:"+ referer;
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
		
		//put rating (if there is one) into the model
		if (googleUserService.isUserLoggedIn()) {
			//get swagSwapUser using email key from available google user
			//we've got to create them here if they don't already exist in our DB
			SwagSwapUser swagSwapUser = swagSwapUserService.findByEmailOrCreate(
											googleUserService.getCurrentUser().getEmail());
//			Map<Long, Integer> userRatings = new HashMap<Long, Integer>(); //swagItemKey, user rating
			model.addAttribute("swagSwapUser", swagSwapUser);
		}
		for (SwagItem swagItem : swagItems) {
			//add backing object for each possible new rating
			model.addAttribute("newRating"+"-"+swagItem.getKey(), new SwagItemRating(swagItem.getKey())); 
		}
		
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