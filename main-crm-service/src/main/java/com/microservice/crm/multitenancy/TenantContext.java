package com.microservice.crm.multitenancy;

/**
 * Holds the current tenant key per request thread. MUST be cleared in the
 * finally block of every filter that sets it.
 */
public class TenantContext {

	private static final ThreadLocal<String> CURRENT_TENANT_KEY = new ThreadLocal<>();
	private static final ThreadLocal<String> CURRENT_USER_ID = new ThreadLocal<>();
	private static final ThreadLocal<String> CURRENT_USER_ROLE = new ThreadLocal<>();

	// ── Tenant Key (e.g. "CRM", "DEFAULT", "BakeMax") ────────────────────────

	public static void setTenantKey(String tenantKey) {
		CURRENT_TENANT_KEY.set(tenantKey);
	}

	public static String getTenantKey() {
		return CURRENT_TENANT_KEY.get();
	}

	// ── User info ─────────────────────────────────────────────────────────────

	public static void setUserId(String userId) {
		CURRENT_USER_ID.set(userId);
	}

	public static String getUserId() {
		return CURRENT_USER_ID.get();
	}

	public static void setUserRole(String role) {
		CURRENT_USER_ROLE.set(role);
	}

	public static String getUserRole() {
		return CURRENT_USER_ROLE.get();
	}

	// ── Lifecycle ─────────────────────────────────────────────────────────────

	/**
	 * Set all context values in one call.
	 */
	public static void set(String tenantKey, String userId, String role) {
		CURRENT_TENANT_KEY.set(tenantKey);
		CURRENT_USER_ID.set(userId);
		CURRENT_USER_ROLE.set(role);
	}

	/**
	 * ALWAYS call this in finally block — prevents tenant leak across thread pool
	 * threads.
	 */
	public static void clear() {
		CURRENT_TENANT_KEY.remove();
		CURRENT_USER_ID.remove();
		CURRENT_USER_ROLE.remove();
	}
}
