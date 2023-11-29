package com.mshop.mailservice;

import java.io.IOException;

import javax.mail.MessagingException;

import com.mshop.models.MailInfo;

public interface SendMailService {

	void run();

	void queue(String to, String subject, String body);

	void queue(MailInfo mail);

	void send(MailInfo mail) throws MessagingException, IOException;

}
