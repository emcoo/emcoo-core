package com.emcoo.ef.notification;

import com.emcoo.ef.notification.template.MobileMessageTemplate;

/**
 * Sms Channel Interface
 *
 * @author mark
 */
public interface SmsChannel {

	boolean send(MobileMessageTemplate mobileMessageTemplate);

}