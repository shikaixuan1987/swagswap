package com.swagswap.web.springmvc.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.swagswap.exceptions.ImageTooLargeException;
import com.swagswap.exceptions.InvalidSwagImageException;
import com.swagswap.exceptions.LoadImageFromURLException;
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
		String googleID = swagItem.getOwnerGoogleID();
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
        MimeMessage mimeMessage = new MimeMessage(session, request.getInputStream());
		log.info("Receieved message");
		String userID = mimeMessage.getSubject();
		if (StringUtils.isEmpty(userID)) {
			return;
		}
		SwagSwapUser user = swagSwapUserService.get(Long.valueOf(userID));
		if (user==null) {
			return;
		}
		// take apart the mutlipart
		
		InputStream inputStreamContent = (InputStream)mimeMessage.getContent();
		//from http://groups.google.com/group/google-appengine-java/browse_thread/thread/e6a23e509e7d43c9/09c5b278e85144ff?lnk=gst&q=incoming+email#09c5b278e85144ff
		ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource
		(inputStreamContent,mimeMessage.getContentType());
		Multipart mimeMultipart = new MimeMultipart(byteArrayDataSource); 
		//TODO get message body
		String messageBody="uploaded item";
		SwagItem swagItem = new SwagItem();
		swagItem.setOwnerGoogleID((user.getGoogleID()));
		swagItem.setOwnerNickName(user.getNickName());
		String swagName = StringUtils.isEmpty(messageBody) ? "uploaded with no name" : messageBody;
		swagItem.setName(swagName); 
		// from http://java.sun.com/developer/onlineTraining/JavaMail/contents.html#JavaMailMessage
		for (int i=0, n=mimeMultipart.getCount(); i<n; i++) {
		  String disposition = mimeMultipart.getBodyPart(i).getDisposition();
		  //TODO get messageBody
//		  BodyPart bodyPart = mimeMultipart.getBodyPart(i);
//		  if (bodyPart.isMimeType("text/*")) {
//			  messageBody=(String)bodyPart.getContent();
//		  }
			if ((disposition != null)
					&& ((disposition.equals(Part.ATTACHMENT) || (disposition
							.equals(Part.INLINE))))) {
				InputStream inputStream = mimeMultipart.getBodyPart(i)
						.getInputStream();
				byte[] imageData = getImageDataFromInputStream(inputStream);
				swagItem.setImageBytes(imageData);
				log.debug("saved emailed item, owner Sam Brodkin");
			}
		}
		itemService.saveFromEmail(swagItem);
		mailService.send(user.getGoogleID(), user.getEmail(), "Your swag item: " + swagName +
				" has been successfuly created",
		"\n\n<br/><br/>See Your Item here: (Spring MVC impl) http://swagswap.appspot.com/springmvc/view/" +
		swagItem.getKey() +
		"\n<br/>or here (JSF 2.0 impl) http://swagswap.appspot.com/jsf/viewSwag.jsf?swagItemKey=" +
		swagItem.getKey()
		);
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	// tsk tsk, this is copy pasted from ItemService.getImageDataFromURL()
	public byte[] getImageDataFromInputStream(InputStream inputStream)
			throws LoadImageFromURLException, ImageTooLargeException {
		BufferedInputStream bis = null;
		ByteArrayOutputStream bos = null;
		try {
			bis = new BufferedInputStream(inputStream);
			// write it to a byte[] using a buffer since we don't know the exact
			// image size
			byte[] buffer = new byte[1024];
			bos = new ByteArrayOutputStream();
			int i = 0;
			while (-1 != (i = bis.read(buffer))) {
				bos.write(buffer, 0, i);
			}
			byte[] imageData = bos.toByteArray();
			if (imageData.length > 150000) {
				throw new ImageTooLargeException("from email", 150000);
			}
			return imageData;
		} catch (IOException e) {
			throw new InvalidSwagImageException(e);
		} finally {
			try {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public void startPage (HttpServletResponse response) throws IOException {
		response.sendRedirect("/jsf/home.jsf");
	}
}