package com.emcoo.ef.common.config.autoconfigure;

import com.emcoo.ef.common.config.properties.CommonProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Common Auto Configuration
 *
 * @author mark
 */
@Configuration
@EnableConfigurationProperties(CommonProperties.class)
public class CommonAutoConfiguration {

	@Autowired
	private CommonProperties commonProperties;

	@Bean
	@ConditionalOnMissingBean
	public CommonProperties commonProperties() {
		return new CommonProperties();
	}

}

