package com.swagswap.service;

import org.apache.log4j.Logger;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;

/**
 * For Resizing images
 * 
 * @author scott
 * 
 */
public class ImageServiceImpl implements ImageService {

	private static final Logger log = Logger
			.getLogger(ImageServiceImpl.class);
	
	private static final int THUMBNAIL_WIDTH = 66;
	private static final int THUMBNAIL_HEIGHT = 50;

	private static final int IMAGE_WIDTH = 283;
	private static final int IMAGE_HEIGHT = 212;

	public ImageServiceImpl() {
	}


	public byte[] getResizedImageBytes(byte[] imageBytes) {
		return resizeImage(imageBytes, IMAGE_WIDTH, IMAGE_HEIGHT);
	}

	public byte[] getThumbnailBytes(byte[] imageBytes) {
		return resizeImage(imageBytes, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
	}

	private byte[] resizeImage(byte[] imageBytes, int resizedWidth,
			int resizedHeight) {

		ImagesService imagesService = ImagesServiceFactory.getImagesService();
		Image oldImage = ImagesServiceFactory.makeImage(imageBytes);
		Transform resize = ImagesServiceFactory.makeResize(resizedWidth,
				resizedHeight);
		Image newImage = imagesService.applyTransform(resize, oldImage,
				ImagesService.OutputEncoding.valueOf("JPEG"));
		return newImage.getImageData();
	}
	
	
}