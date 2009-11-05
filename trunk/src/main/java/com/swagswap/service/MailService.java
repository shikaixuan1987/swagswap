package com.swagswap.service;

public interface MailService {

	public abstract void sendMail(String email, String subject, String msgBody);
}