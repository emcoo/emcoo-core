package com.emcoo.ef.restful.service;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Tenant ACL
 *
 * @author mark
 */
@Component
public class TenantAcl {

	@Autowired
	private HttpServletRequest request;

	private String defaultRolePrefix = "ROLE_";
	private String defaultPermissionPrefix = "OP_";
	protected Authentication authentication;
	protected String tenantId;
	private Set<String> roles;
	private RoleHierarchy roleHierarchy;

	public void init() {
		this.authentication = SecurityContextHolder.getContext().getAuthentication();
		this.tenantId = this.getTenantId();
	}

	private String getTenantId() {
		try {
			return BeanUtils.getProperty(request.getAttribute("tenant"), "id");
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NullPointerException e) {
			throw new RuntimeException(e);
		}
	}

	public final boolean hasRole(String role) {
		return this.hasAnyRole(role);
	}

	public final boolean hasAnyRole(String... roles) {
		return this.hasAnyAuthorityName(this.defaultRolePrefix, roles);
	}

	public final boolean hasPermission(String permission) {
		return this.hasAnyPermission(permission);
	}

	public final boolean hasAnyPermission(String... permissions) {
		return this.hasAnyAuthorityName(this.defaultPermissionPrefix, permissions);
	}

	private boolean hasAnyAuthorityName(String prefix, String... roles) {
		this.init();

		Set<String> roleSet = this.getAuthoritySet();

		int roleLength = roles.length;
		for (int i = 0; i < roleLength; ++i) {
			String role = roles[i];
			String defaultedRole = getRoleWithDefaultPrefix(prefix, role);
			if (roleSet.contains(defaultedRole)) {
				return true;
			}
		}
		return false;
	}

	private Set<String> getAuthoritySet() {
		if (this.roles == null) {
			this.roles = new HashSet();
			Collection<? extends GrantedAuthority> userAuthorities = this.authentication.getAuthorities();
			if (this.roleHierarchy != null) {
				userAuthorities = this.roleHierarchy.getReachableGrantedAuthorities(userAuthorities);
			}

			this.roles = AuthorityUtils.authorityListToSet(userAuthorities);
		}

		return this.roles;
	}

	private static String getRoleWithDefaultPrefix(String defaultRolePrefix, String role) {
		if (role == null) {
			return role;
		} else if (defaultRolePrefix != null && defaultRolePrefix.length() != 0) {
			return role.startsWith(defaultRolePrefix) ? role : defaultRolePrefix + role;
		} else {
			return role;
		}
	}

	public void setDefaultRolePrefix(String defaultRolePrefix) {
		this.defaultRolePrefix = defaultRolePrefix;
	}

	public void setDefaultPermissionPrefix(String defaultPermissionPrefix) {
		this.defaultPermissionPrefix = defaultPermissionPrefix;
	}

	public void setRoleHierarchy(RoleHierarchy roleHierarchy) {
		this.roleHierarchy = roleHierarchy;
	}
}