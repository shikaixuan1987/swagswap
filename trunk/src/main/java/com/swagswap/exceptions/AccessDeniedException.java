package com.swagswap.exceptions;



public class AccessDeniedException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	//Needed for GWT compilation
	public AccessDeniedException() {
		super();
	}
	
	public AccessDeniedException(String msg) {
		super(msg);
	}

	public AccessDeniedException(String itemName, String itemOwnerEmail, String emailRequestingPermision) {
		this("Access denied to " + itemName 
				+ " for " + emailRequestingPermision + ". owner is " 
				+ itemOwnerEmail
				);
	}

}
