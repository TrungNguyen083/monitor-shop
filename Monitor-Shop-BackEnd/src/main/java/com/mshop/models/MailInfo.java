package com.mshop.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailInfo {
	String from;
	String to;
	String subject;
	String body;
	String attachments;

	public MailInfo(String to, String subject, String body) {
		this.from = "Monitor Shop <nguyennguyen6616634@gmail.com>";
		this.to = to;
		this.subject = subject;
		this.body = body;
	}
	
}
