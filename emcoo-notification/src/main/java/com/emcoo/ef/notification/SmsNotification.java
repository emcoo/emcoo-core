package com.emcoo.ef.notification;

import com.emcoo.ef.notification.template.MobileMessageTemplate;

/**
 * Sms Notification Interface
 *
 * @author mark
 */
public interface SmsNotification {

	boolean send(MobileMessageTemplate mobileMessageTemplate);
}
