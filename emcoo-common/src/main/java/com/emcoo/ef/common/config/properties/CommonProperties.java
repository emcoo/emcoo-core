package com.emcoo.ef.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * System Properties
 *
 * @author mark
 */
@Data
@ConfigurationProperties(prefix = "emcoo.common")
public class CommonProperties {

	private String siteName;

	private String siteUrl;

	private String siteEmail;
}