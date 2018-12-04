package com.emcoo.ef.notification.config.autoconfigure;

import com.emcoo.ef.notification.config.properties.MailModelProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Storage Auto Configuration
 *
 * @author mark
 */
@Configuration
@EnableConfigurationProperties(MailModelProperties.class)
public class MailAutoConfiguration {

	@Autowired
	private MailModelProperties mailModelProperties;

	@Bean
	@ConditionalOnMissingBean
	public MailModelProperties mailModelProperties() {
		return new MailModelProperties();
	}

}

