package com.swagswap.exceptions;


public class LoadImageFromURLException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String url;

	public LoadImageFromURLException(String url, Throwable cause) {
		super("Cannot load image from " + url, cause);
	}

	public String getUrl() {
		return url;
	}
}
