package com.emcoo.ef.restful.config.autoconfigure;

import com.emcoo.ef.restful.TenantAuthenticationConverter;
import com.emcoo.ef.restful.interceptor.TenantResolveInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Tenant Auto Configuration
 *
 * @author mark
 */
@Configuration
public class TenantAutoConfiguration implements WebMvcConfigurer {

	@Autowired
	private ApplicationContext context;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(tenantResolveInterceptor());
	}

	@Bean
	public TenantResolveInterceptor tenantResolveInterceptor() {
		return new TenantResolveInterceptor(context.getBean(TenantAuthenticationConverter.class));
	}

}
