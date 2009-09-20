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

import com.swagswap.domain.DataFormat;
import com.swagswap.domain.Protocol;
import com.swagswap.domain.SwagImage;
import com.swagswap.domain.SwagItem;
import com.swagswap.service.SwagItemService;

@Controller
public class SwagItemController {
	private static final Logger log = Logger.getLogger(SwagItemController.class);

	@Autowired
	private SwagItemService swagItemService;

	@ModelAttribute("availableDataFormats")
	public DataFormat[] populateDataFormats() {
		return DataFormat.values();
	}

	@ModelAttribute("availableProtocols")
	public Protocol[] populateProtocols() {
		return Protocol.values();
	}

	@RequestMapping(value = "/swagItems", method = RequestMethod.GET)
	public String getAllHandler(Model model) {
		model.addAttribute("swagItem", new SwagItem());
		model.addAttribute("swagItems", swagItemService.getAll());
		return "swagItems";
	}

	@RequestMapping(value = "/swagItem/add", method = RequestMethod.GET)
	public String addHandler(Model model) {
		model.addAttribute("swagItem", new SwagItem());
		return "swagItems";
	}

	@RequestMapping(value = "/swagItem/delete/{key}", method = RequestMethod.GET)
	public String deleteHandler(@PathVariable("key") Long key) {
		swagItemService.delete(key);
		return "redirect:/swag/swagItems";
	}

	@RequestMapping(value = "/swagItem/edit/{key}", method = RequestMethod.GET)
	public String editHandler(@PathVariable("key") Long key, Model model) {
		SwagItem swagItem = swagItemService.get(key);
		model.addAttribute("swagItem", swagItemService.get(key));
		return "swagItems";
	}

	@RequestMapping(value = "/swagItem/save", method = RequestMethod.POST)
	public String saveHandler(@ModelAttribute SwagItem swagItem) {
//		handleImageUpload(swagItem);
		swagItem.setImage(new SwagImage(swagItem.getImageBytes()));
		swagItemService.save(swagItem);
		return "redirect:/swag/swagItems";
	}

//	private void handleImageUpload(SwagItem swagItem) {
//		if (swagItem.getImageBytes()==null) {
//			log.debug("No image uploaded");
//		}
//		else {
//			SwagImage swagImage = new SwagImage(swagItem.getImageBytes());
//			swagImage.filename="foo";
////			swagItem.setImage(new SwagImage(swagItem.getImageBytes()));
//		
//		}
//	}

	@RequestMapping(value = "/swagItem/search", method = RequestMethod.POST)
	public String searchHandler(Model model, @ModelAttribute SwagItem swagItem) {
		Collection<SwagItem> swagItems = swagItemService.search(swagItem.getName());
		model.addAttribute("swagItems", swagItems);
		return "swagItems";
	}

	// TODO is this needed?
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

	public void setSwagItemService(SwagItemService swagItemService) {
		this.swagItemService = swagItemService;
	}

}