package com.emcoo.ef.notification.config.autoconfigure;

import com.emcoo.ef.notification.DefaultMailNotification;
import com.emcoo.ef.notification.MailNotification;
import com.emcoo.ef.notification.channel.MailChannel;
import com.emcoo.ef.notification.config.properties.MailModelProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * Storage Auto Configuration
 *
 * @author mark
 */
@Configuration
@EnableConfigurationProperties({MailModelProperties.class})
@Import(MailSenderAutoConfiguration.class)
public class MailAutoConfiguration {

	@Autowired
	private MailModelProperties mailModelProperties;

	@Autowired
	private MailProperties mailProperties;

	@Bean
	@ConditionalOnMissingBean
	public MailModelProperties mailModelProperties() {
		return new MailModelProperties();
	}

	@Bean
	@Primary
	public MailNotification defaultMailNotification(MailProperties mailProperties, MailModelProperties mailModelProperties, JavaMailSender mailSender) {
		return new DefaultMailNotification(new MailChannel(
				mailProperties,
				mailModelProperties,
				mailSender
		));
	}

}

