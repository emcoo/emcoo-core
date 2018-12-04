package com.emcoo.ef.notification;

import com.emcoo.ef.notification.template.MobileMessageTemplate;
import lombok.Getter;
import lombok.Setter;

/**
 * Default File System Impl
 *
 * @author mark
 */
@Getter
@Setter
public class YunpianSmsNotification implements SmsNotification {

	protected SmsChannel channel;

	public YunpianSmsNotification(SmsChannel channel) {
		this.channel = channel;
	}

	@Override
	public boolean send(MobileMessageTemplate mobileMessageTemplate) {
		return this.channel.send(mobileMessageTemplate);
	}
}

