package com.emcoo.ef.notification;

import com.emcoo.ef.notification.template.MailMessageTemplate;

/**
 * Mail Notification Interface
 *
 * @author mark
 */
public interface MailNotification {

	boolean send(MailMessageTemplate mailMessageTemplate);

}
