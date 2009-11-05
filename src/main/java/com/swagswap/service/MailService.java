package com.swagswap.service;

public interface MailService {

	public abstract void send(String googleID, String email, String subject, String msgBody);
	
	public abstract void sendWithTaskManager(Long swagItemKey, String subject, String msgBody);
}