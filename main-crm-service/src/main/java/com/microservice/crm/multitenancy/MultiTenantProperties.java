package com.microservice.crm.multitenancy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "edue.multitenancy")
public class MultiTenantProperties {

	/**
	 * tenantKey → tenantId UUID e.g. "CRM" → "32d3b08f-6735-43dd-b4a1-64cb110d2039"
	 */
	private Map<String, String> tenantKeyVsId;

	/**
	 * tenantKey → datasource connection properties
	 */
	private Map<String, DataSourceProperties> tenantAndDataSourceProperties;

	// ── Getters & Setters ─────────────────────────────────────────────────────

	public Map<String, String> getTenantKeyVsId() {
		return tenantKeyVsId;
	}

	public void setTenantKeyVsId(Map<String, String> tenantKeyVsId) {
		this.tenantKeyVsId = tenantKeyVsId;
	}

	public Map<String, DataSourceProperties> getTenantAndDataSourceProperties() {
		return tenantAndDataSourceProperties;
	}

	public void setTenantAndDataSourceProperties(Map<String, DataSourceProperties> tenantAndDataSourceProperties) {
		this.tenantAndDataSourceProperties = tenantAndDataSourceProperties;
	}

	/**
	 * Resolve tenantKey from a tenantId UUID. Used when token carries a UUID and we
	 * need the key for DataSource routing.
	 */
	public String resolveKeyFromId(String tenantId) {
		if (tenantId == null)
			return "DEFAULT";
		return tenantKeyVsId.entrySet().stream().filter(e -> e.getValue().equalsIgnoreCase(tenantId))
				.map(Map.Entry::getKey).findFirst().orElse("DEFAULT");
	}

	// ── Nested config class ───────────────────────────────────────────────────

	public static class DataSourceProperties {
		private String url;
		private String driverClassName;
		private String username;
		private String password;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getDriverClassName() {
			return driverClassName;
		}

		public void setDriverClassName(String v) {
			this.driverClassName = v;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}
}
