package com.emcoo.ef.restful.exception;

import org.springframework.security.core.AuthenticationException;

public class TenantNotFoundException extends AuthenticationException {
	public TenantNotFoundException(String msg) {
		super(msg);
	}

	public TenantNotFoundException(String msg, Throwable t) {
		super(msg, t);
	}
}
