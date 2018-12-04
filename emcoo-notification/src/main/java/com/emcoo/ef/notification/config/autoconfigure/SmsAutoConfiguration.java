package com.emcoo.ef.notification.config.autoconfigure;

import com.emcoo.ef.notification.SmsNotification;
import com.emcoo.ef.notification.YunpianSmsNotification;
import com.emcoo.ef.notification.channel.YunpianSmsChannel;
import com.emcoo.ef.notification.config.properties.MailModelProperties;
import com.emcoo.ef.notification.config.properties.YunpainSmsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Storage Auto Configuration
 *
 * @author mark
 */
@Configuration
@EnableConfigurationProperties(YunpainSmsProperties.class)
public class SmsAutoConfiguration {

	@Autowired
	private YunpainSmsProperties yunpainSmsProperties;

	@Bean
	@ConditionalOnMissingBean
	public YunpainSmsProperties yunpainSmsProperties() {
		return new YunpainSmsProperties();
	}

	@Bean
	@Primary
	public SmsNotification yunpianSmsNotification(YunpainSmsProperties yunpainSmsProperties) {
		return new YunpianSmsNotification(new YunpianSmsChannel(yunpainSmsProperties));
	}
}

