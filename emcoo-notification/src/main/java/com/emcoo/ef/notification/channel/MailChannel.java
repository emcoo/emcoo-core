package com.emcoo.ef.notification.channel;

import com.emcoo.ef.notification.config.properties.MailModelProperties;
import com.emcoo.ef.notification.template.MailMessageTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * Email Channel
 *
 * @author mark
 */
public class MailChannel implements com.emcoo.ef.notification.MailChannel {

	private final Logger LOGGER = LoggerFactory.getLogger(MailChannel.class);

	MailProperties mailProperties;

	MailModelProperties mailModelProperties;

	JavaMailSender emailSender;

	public MailChannel(MailProperties mailProperties, MailModelProperties mailModelProperties, JavaMailSender javaMailSender) {
		this.mailProperties = mailProperties;
		this.mailModelProperties = mailModelProperties;
		this.emailSender = javaMailSender;
	}

	/**
	 * Send mail
	 *
	 * @param mailMessageTemplate
	 */
	@Override
	public boolean send(MailMessageTemplate mailMessageTemplate) {
		try {
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom(mailModelProperties.getDefaultSenderAddress(), mailModelProperties.getDefaultSender());
			helper.setTo(mailMessageTemplate.getTo());
			helper.setSubject(mailMessageTemplate.getSubject());
			helper.setText(mailMessageTemplate.getText());

			if (mailMessageTemplate.getAttachment() != null) {
				for (String f : mailMessageTemplate.getAttachment()) {
					FileSystemResource file = new FileSystemResource(new File(f));
					helper.addAttachment(file.getFilename(), file);
					helper.addAttachment(file.getFilename(), file);
				}
			}

			emailSender.send(message);
			LOGGER.info(String.format("[Notification] Sent email from %s to %s", mailModelProperties.getDefaultSenderAddress(), mailMessageTemplate.getTo()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			LOGGER.error("[Notification] Send email failed", e);
		} catch (MessagingException e) {
			e.printStackTrace();
			LOGGER.error("[Notification] Send email failed", e);
		}

		return true;
	}
}
