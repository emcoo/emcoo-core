package com.emcoo.ef.notification.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Email Properties
 *
 * @author mark
 */
@Data
@ConfigurationProperties(prefix = "emcoo.notification.mail")
public class MailModelProperties {

	/**
	 * 默认发送者名称
	 */
	private String defaultSender;

	/**
	 * 默认发送者邮件地址
	 */
	private String defaultSenderAddress;
}
