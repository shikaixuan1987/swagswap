package com.swagswap.web.springmvc.controller;

import java.io.IOException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.swagswap.domain.SwagItem;
import com.swagswap.domain.SwagSwapUser;
import com.swagswap.service.AdminService;
import com.swagswap.service.ItemService;
import com.swagswap.service.MailService;
import com.swagswap.service.SwagSwapUserService;

@Controller
public class AdminController {
	private static final Logger log = Logger.getLogger(AdminController.class);
	private AdminService adminService;
	private SwagSwapUserService swagSwapUserService;
	private ItemService itemService;
	private MailService mailService;
	
	@Autowired
	public AdminController(AdminService adminService, 
			SwagSwapUserService swagSwapUserService, 
			ItemService itemService,
			MailService mailService) {
		this.adminService = adminService;
		this.swagSwapUserService=swagSwapUserService;
		this.itemService=itemService;
		this.mailService=mailService;
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
	
	@RequestMapping(value = "/admin/blackListUser", method = RequestMethod.GET)
	public String blackListUser(@RequestParam("email") String email,
					Model model) throws IOException {
		swagSwapUserService.blackListUser(email);
		model.addAttribute("message", "blacklisted " + email);
		return "admin";
	}
	
	@RequestMapping(value = "/opt-out/{googleID}/{optOut}", method = RequestMethod.GET)
	public String optOut(
			@PathVariable("googleID") String googleId,
			@PathVariable("optOut") boolean optOut,
			Model model) throws IOException {
		swagSwapUserService.optOut(googleId, optOut);
		//repace it in the session
		
		model.addAttribute("message", "You have been" +
				((optOut)? " removed from" : " added to") + " future SwagSwap mailings.");
		return "opt-out";
	}
	
	/**
	 * Construct mail message and send it using the MailService
	 * Called by Task Queue
	 * @param swagItemKey
	 * @param subject
	 * @param msgBody
	 * @param response returns HTTP 200 response status
	 * @param model
	 * @throws IOException
	 */
	@RequestMapping(value = "/admin/sendMail", method = RequestMethod.POST)
	public void sendMailById(
			@RequestParam("swagItemKey") String swagItemKey,
			@RequestParam("subject") String subject,
			@RequestParam("msgBody") String msgBody,
			HttpServletResponse response,
			Model model) throws IOException {
		SwagItem swagItem = itemService.get(Long.parseLong(swagItemKey));
		String googleID = swagItem.getOwnerID();
		SwagSwapUser swagSwapUser = swagSwapUserService.findByGoogleID(googleID);
		//TODO don't send emails when a user comments on their own item
		
		if (swagSwapUser.getOptOut()) {
			log.debug(swagSwapUser.getGoogleID() + " has opted out of emails");
		}
		else {
			String email = swagSwapUser.getEmail();
			log.debug("Sending mail to " + email + " msg is subject:" + subject + " msgBody:" + msgBody);
			try {
				mailService.send(googleID, email, subject, msgBody);
			}
			catch (Exception e) {
				log.error(e);
			}
		}
		//if you don't do this, task queue retries the task (a lot)
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	//Incoming email see http://code.google.com/appengine/docs/java/mail/receiving.html
	@RequestMapping(value = "/_ah/mail/{address}", method = RequestMethod.POST)
	public void routeIncomingEmail (
			@PathVariable("address") String address, HttpServletRequest request,
			HttpServletResponse response) throws IOException, MessagingException {
		log.debug("Got mail posted to " + address);
		//Handle message
        Properties props = new Properties(); 
        Session session = Session.getDefaultInstance(props, null); 
        MimeMessage message = new MimeMessage(session, request.getInputStream());
		log.info("Message is " + message.getContent());
		
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public void startPage (HttpServletResponse response) throws IOException {
		response.sendRedirect("/welcome.html");
	}
}