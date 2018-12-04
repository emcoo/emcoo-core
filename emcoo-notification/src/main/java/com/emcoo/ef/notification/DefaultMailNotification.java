package com.emcoo.ef.notification;

import com.emcoo.ef.notification.template.MailMessageTemplate;
import lombok.Getter;
import lombok.Setter;

/**
 * Default File System Impl
 *
 * @author mark
 */
@Getter
@Setter
public class DefaultMailNotification implements MailNotification {

	protected MailChannel channel;

	public DefaultMailNotification(MailChannel channel) {
		this.channel = channel;
	}

	@Override
	public boolean send(MailMessageTemplate mailMessageTemplate) {
		return this.channel.send(mailMessageTemplate);
	}
}