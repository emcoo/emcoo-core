package com.emcoo.ef.notification;

import com.emcoo.ef.notification.template.MailMessageTemplate;

/**
 * Sms Channel Interface
 *
 * @author mark
 */
public interface MailChannel {

	boolean send(MailMessageTemplate mailMessageTemplate);

}