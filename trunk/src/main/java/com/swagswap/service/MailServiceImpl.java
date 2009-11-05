package com.swagswap.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * For sending email
 * 
 */
public class MailServiceImpl implements MailService {

	private static final Logger log = Logger.getLogger(MailServiceImpl.class);

	@Autowired
	private ItemService swagSwItemService;
	
	public MailServiceImpl() {
	}

	public void sendMail(String email, String subject, String msgBody) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        String sender= "SamBrodkin@gmail.com";
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(sender));
            msg.addRecipient(Message.RecipientType.TO,new InternetAddress(email));
            msg.setSubject(subject);
            msg.setText(msgBody);
            Transport.send(msg);
            log.debug("sending mail to " + email);
        } catch (Exception e) {
           log.error("sender is " +  sender, e);
        }
	}

}