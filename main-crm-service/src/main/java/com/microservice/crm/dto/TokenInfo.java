package com.microservice.crm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response DTO from auth-service /auth/validate endpoint. Also used internally
 * when resolving context from gateway headers.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenInfo {

	private boolean valid;

	/** The tenant key e.g. "CRM", "DEFAULT" — resolved by auth-service */
	private String tenantKey;

	/** The tenantId UUID e.g. "32d3b08f-6735-43dd-b4a1-64cb110d2039" */
	private String tenantId;

	private String userId;
	private String username;
	private String role;

	// ── Constructors ──────────────────────────────────────────────────────────

	public TokenInfo() {
	}

	public TokenInfo(boolean valid, String tenantKey, String tenantId, String userId, String username, String role) {
		this.valid = valid;
		this.tenantKey = tenantKey;
		this.tenantId = tenantId;
		this.userId = userId;
		this.username = username;
		this.role = role;
	}

	/** Convenience: create an invalid/empty TokenInfo */
	public static TokenInfo invalid() {
		return new TokenInfo(false, null, null, null, null, null);
	}

	// ── Getters & Setters ─────────────────────────────────────────────────────

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getTenantKey() {
		return tenantKey;
	}

	public void setTenantKey(String tenantKey) {
		this.tenantKey = tenantKey;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}