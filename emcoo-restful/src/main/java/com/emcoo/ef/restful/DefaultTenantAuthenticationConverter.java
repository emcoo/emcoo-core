package com.emcoo.ef.restful;

import com.emcoo.ef.restful.tenantdetails.TenantDetails;
import com.emcoo.ef.restful.tenantdetails.TenantDetailsService;

/**
 * Default Tenant Auth Converter
 *
 * @author mark
 */
public class DefaultTenantAuthenticationConverter implements TenantAuthenticationConverter {

	private TenantDetailsService tenantDetailsService;

	public DefaultTenantAuthenticationConverter() {
	}

	public void setTenantDetailsService(TenantDetailsService tenantDetailsService) {
		this.tenantDetailsService = tenantDetailsService;
	}

	@Override
	public TenantDetails extractAuthentication(String tenantId) {
		return this.tenantDetailsService.loadTenantById(tenantId);
	}
}
