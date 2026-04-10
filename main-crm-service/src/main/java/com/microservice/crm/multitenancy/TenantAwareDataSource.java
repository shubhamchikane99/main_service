package com.microservice.crm.multitenancy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Routes every JPA/JDBC call to the correct DataSource based on the tenantKey
 * stored in TenantContext for the current thread.
 */
public class TenantAwareDataSource extends AbstractRoutingDataSource {

	private static final Logger log = LoggerFactory.getLogger(TenantAwareDataSource.class);

	@Override
	protected Object determineCurrentLookupKey() {
		String tenantKey = TenantContext.getTenantKey();
		log.debug("Routing DataSource to tenant: {}", tenantKey);
		return tenantKey != null ? tenantKey : "DEFAULT";
	}
}