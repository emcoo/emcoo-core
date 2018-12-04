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
	 * Default Sender Name
	 */
	private String defaultSender;

	/**
	 * Default Sender Address
	 */
	private String defaultSenderAddress;
}
