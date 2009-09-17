package com.itcommand.web.controller;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.itcommand.dao.SwagImageDao;

@Controller
public class SwagImageController {
	private static final Logger log = Logger.getLogger(SwagImageController.class);

	private SwagImageDao swagImageDao;

	@Autowired
	public SwagImageController(SwagImageDao swagImageDao) {
		this.swagImageDao = swagImageDao;
	}

	@RequestMapping("/imageList")
	public String showImageList(ModelMap model) {
		model.addAttribute("images", this.swagImageDao.getAll());
		return "imageList";
	}
	
	@RequestMapping(value = "/showImage/{key}", method = RequestMethod.GET)
	public void streamImageContent(@PathVariable("key") String key, OutputStream outputStream) throws IOException {
		swagImageDao.streamImage(key, outputStream);
	}

	
//	@RequestMapping("/imageUpload")
//	public String processImageUpload(@RequestParam("name") String name,
//			@RequestParam("description") String description, @RequestParam("image") MultipartFile image)
//			throws IOException {
//
//		this.imageDatabase.storeImage(name, image.getInputStream(), (int) image.getSize(), description);
//		return "redirect:imageList";
//	}

}