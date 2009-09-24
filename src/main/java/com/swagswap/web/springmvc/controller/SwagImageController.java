package com.swagswap.web.springmvc.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.swagswap.dao.SwagImageDao;
import com.swagswap.domain.SwagImage;

@Controller
public class SwagImageController {
	private static final Logger log = Logger.getLogger(SwagImageController.class);
	public static final String PATH_TO_DEFAULT_IMAGE = "images/no_photo.jpg";
	private static byte[] defaultImage;

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
	
	/**
	 * Stream SwagImage.image from database or show defaultImage if there is none
	 */
	@RequestMapping(value = "/showImage/{key}", method = RequestMethod.GET)
	public void streamImageContent(@PathVariable("key") String key,
					HttpServletRequest req, OutputStream outputStream) throws IOException {
		SwagImage swagImage = swagImageDao.get(key);
		byte[] swagImageBytes;
		//if there's not image, return default image
		if (swagImage.getImage()==null) {
			swagImageBytes=getDefaultImage(req.getRequestURL().toString());
		}
		else {
			swagImageBytes = swagImageDao.get(key).getImage().getBytes();
		}
		try {
			outputStream.write(swagImageBytes, 0, swagImageBytes.length);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * This is the only way I can get the base URL to this app
	 * @param req
	 * @return
	 */
	protected String constructDefaultImageURL(String requestURL) {
		String baseURL = requestURL.substring(0,requestURL.lastIndexOf("/swag"));
		return baseURL + "/" + PATH_TO_DEFAULT_IMAGE;
	}

	/**
	 * Lazy load default image for use if SwagItem isn't created with an image
	 * ImageIO had a nice way to do it but it is blacklisted on appengine :(
	 * @param requestURL to construct the full image URL 
	 */
	private byte[] getDefaultImage(String requestURL) {
		if (defaultImage != null && defaultImage.length != 0) {
			return defaultImage;
		}
		String defaultImageURLString = constructDefaultImageURL(requestURL);
		ByteArrayOutputStream bas = null;

		//create defaultImage byte[] from URL
		//ouch this would have been easier with ImageIO!
		try {
			URL defaultImageURL = new URL(defaultImageURLString);
			BufferedInputStream bis = new BufferedInputStream(defaultImageURL.openStream());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int i;
			while ((i = bis.read()) != -1) {
				baos.write(i);
			}
			defaultImage = baos.toByteArray();
			return defaultImage;
		} catch (IOException e) {
			log.error("couldn't load defaultImage at " + defaultImageURLString, e);
			return null;
		} finally {
			try {
				if (bas != null)
					bas.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}
	


}