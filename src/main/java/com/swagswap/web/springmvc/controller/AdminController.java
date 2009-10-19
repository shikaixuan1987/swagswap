package com.swagswap.web.springmvc.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.swagswap.service.AdminService;

@Controller
public class AdminController {
	private static final Logger log = Logger.getLogger(AdminController.class);
	private AdminService adminService;

	@Autowired
	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}

	@RequestMapping("/admin/main")
	public String adminMain() {
		return "admin";
	}
	
	/**
	 * populate images
	 */
	@RequestMapping(value = "/admin/populateTestSwagItems", method = RequestMethod.GET)
	public String populateTestSwagItems(@RequestParam("numberOfSwagItems") int numberOfSwagItems,
					Model model) throws IOException {
		adminService.populateTestSwagItems(numberOfSwagItems);
		model.addAttribute("message", "added " + numberOfSwagItems + " swag items");
		return "admin";
	}
	
	@RequestMapping(value = "/admin/deleteTestSwagItems", method = RequestMethod.GET)
	public String deleteTestSwagItems(Model model) throws IOException {
		int numberDeleted = adminService.deleteTestSwagItems();
		model.addAttribute("message", "deleted " + numberDeleted + " test swag items");
		return "admin";
	}

}