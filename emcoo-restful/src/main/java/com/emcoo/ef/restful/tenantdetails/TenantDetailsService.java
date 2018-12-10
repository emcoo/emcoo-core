package com.emcoo.ef.restful.tenantdetails;

import com.emcoo.ef.restful.exception.TenantNotFoundException;

public interface TenantDetailsService {
	TenantDetails loadTenantById(String var1) throws TenantNotFoundException;
}
