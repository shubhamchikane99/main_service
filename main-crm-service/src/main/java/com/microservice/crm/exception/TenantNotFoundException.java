package com.microservice.crm.exception;

public class TenantNotFoundException extends RuntimeException {
	public TenantNotFoundException(String tenantKey) {
		super("Tenant not found: " + tenantKey);
	}
}
