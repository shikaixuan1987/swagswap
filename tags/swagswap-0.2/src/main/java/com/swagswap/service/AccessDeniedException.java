package com.swagswap.service;

import com.swagswap.domain.SwagItem;

public class AccessDeniedException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public AccessDeniedException(String msg) {
		super(msg);
	}

	public AccessDeniedException(SwagItem swagItem, String emailRequestingPermision) {
		this("Access denied to " + swagItem.getName() 
				+ " for " + emailRequestingPermision + ". owner is " 
				+ swagItem.getOwnerEmail()
				);
	}

}
