package com.microservice.crm.multitenancy;

import org.springframework.stereotype.Component;

import com.microservice.crm.exception.UnauthorizedException;

/**
 * Spring-managed wrapper around TenantContext. Inject this into
 * service/repository classes instead of calling TenantContext directly.
 */
@Component
public class TenantContextHolder {

	public String getCurrentTenantKey() {
		String key = TenantContext.getTenantKey();
		if (key == null || key.isBlank()) {
			throw new UnauthorizedException("No tenant context found for current request");
		}
		return key;
	}

	public String getCurrentUserId() {
		return TenantContext.getUserId();
	}

	public String getCurrentUserRole() {
		return TenantContext.getUserRole();
	}

	public boolean hasTenantContext() {
		String key = TenantContext.getTenantKey();
		return key != null && !key.isBlank();
	}
}
