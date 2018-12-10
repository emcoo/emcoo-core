package com.emcoo.ef.restful;

import com.emcoo.ef.restful.tenantdetails.TenantDetails;

public interface TenantAuthenticationConverter {
	TenantDetails extractAuthentication(String tenantId);
}