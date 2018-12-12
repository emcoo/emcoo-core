package com.emcoo.ef.restful.interceptor;

import com.emcoo.ef.restful.TenantAuthenticationConverter;
import com.emcoo.ef.restful.annotation.RootResource;
import com.emcoo.ef.restful.annotation.TenantResource;
import com.emcoo.ef.restful.tenantdetails.TenantDetails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TenantResolveInterceptor
 *
 * @author mark
 */
public class TenantResolveInterceptor extends HandlerInterceptorAdapter {

	public static final String TENANT_HEADER_KEY = "X-TENANT-ID";

	private TenantAuthenticationConverter tenantTokenConverter;

	public TenantResolveInterceptor(TenantAuthenticationConverter tenantTokenConverter) {
		this.tenantTokenConverter = tenantTokenConverter;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return super.preHandle(request, response, handler);
		}

		String tenantId = request.getHeader(TENANT_HEADER_KEY);
		// restrict the access
		HandlerMethod method = (HandlerMethod) handler;
		TenantResource tenantResource = method.getMethodAnnotation(TenantResource.class);
		RootResource rootResource = method.getMethodAnnotation(RootResource.class);

		boolean isRootResource = false;

		// get annotation from class when no annotation is specified
		if (tenantResource == null && rootResource == null) {
			Class<?> beanType = method.getBeanType();
			tenantResource = AnnotationUtils.findAnnotation(method.getBeanType(), TenantResource.class);
			rootResource = AnnotationUtils.findAnnotation(method.getBeanType(), RootResource.class);
		}

		// still with no annotation, set default
		if (tenantResource == null && rootResource == null) {
			isRootResource = true;
		}

		// tenant resource
		if (tenantResource != null) {
			if (tenantId == null | StringUtils.isBlank(tenantId)) {
				throw new NoHandlerFoundException(request.getMethod(), request.getRequestURI(), null);
			}

			TenantDetails tenantDetails = tenantTokenConverter.extractAuthentication(tenantId);

			if (tenantDetails == null) {
				throw new NoHandlerFoundException(request.getMethod(), request.getRequestURI(), null);
			}
			request.setAttribute("tenant", tenantDetails);
		}

		// root resource
		if ((rootResource != null || isRootResource) && tenantId != null) {
			throw new NoHandlerFoundException(request.getMethod(), request.getRequestURI(), null);
		}

		return super.preHandle(request, response, handler);
	}

}