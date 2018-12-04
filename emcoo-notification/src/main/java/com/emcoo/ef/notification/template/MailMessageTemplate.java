package com.emcoo.ef.notification.template;

import lombok.Data;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Date;

/**
 * 邮件信息模板
 *
 * @author mark
 */
@Data
public class MailMessageTemplate extends BaseMessageTemplate implements Serializable {

	private String from;
	private String replyTo;
	private String[] to;
	private String[] cc;
	private String[] bcc;
	private Date sentDate;
	private String subject;
	private String text;
	private String[] attachment;

	public MailMessageTemplate() {
	}

	public MailMessageTemplate(MailMessageTemplate original) {
		Assert.notNull(original, "'original' message argument must not be null");
		this.from = original.getFrom();
		this.replyTo = original.getReplyTo();
		this.to = BaseMessageTemplate.copyOrNull(original.getTo());
		this.cc = BaseMessageTemplate.copyOrNull(original.getCc());
		this.bcc = BaseMessageTemplate.copyOrNull(original.getBcc());
		this.sentDate = original.getSentDate();
		this.subject = original.getSubject();
		this.text = original.getText();
	}

}