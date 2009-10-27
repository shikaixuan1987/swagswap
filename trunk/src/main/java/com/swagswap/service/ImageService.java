package com.swagswap.service;

public interface ImageService {
	
	byte[] getResizedImageBytes(byte[] imageBytes); 
	byte[] getThumbnailBytes(byte[] imageBytes);

}
