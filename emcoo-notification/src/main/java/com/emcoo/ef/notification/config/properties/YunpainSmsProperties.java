package com.emcoo.ef.notification.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 云片短息 服务配置
 *
 * @author mark
 */
@Data
@ConfigurationProperties(prefix = "emcoo.notification.sms.yunpain")
public class YunpainSmsProperties {
	/**
	 * 应用ID
	 */
	private String accessKey;

	/**
	 * 请求URL
	 */
	private String url;

	/**
	 * 短信模板配置
	 */
	private Map<String, String> channels;
}
