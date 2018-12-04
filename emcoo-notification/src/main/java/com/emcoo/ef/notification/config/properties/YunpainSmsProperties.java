package com.emcoo.ef.notification.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Yunpain SMS Properties
 *
 * @author mark
 */
@Data
@ConfigurationProperties(prefix = "emcoo.notification.sms.yunpain")
public class YunpainSmsProperties {

	private String accessKey;

}
