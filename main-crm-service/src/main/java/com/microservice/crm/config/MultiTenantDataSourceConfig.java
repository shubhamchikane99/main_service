package com.microservice.crm.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.microservice.crm.multitenancy.MultiTenantProperties;
import com.microservice.crm.multitenancy.TenantAwareDataSource;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class MultiTenantDataSourceConfig {

	private static final Logger log = LoggerFactory.getLogger(MultiTenantDataSourceConfig.class);

	@Autowired
	private MultiTenantProperties multiTenantProperties;

	@Bean
	@Primary
	public DataSource dataSource() {
		Map<Object, Object> targetDataSources = new HashMap<>();
		DataSource defaultDataSource = null;

		for (Map.Entry<String, MultiTenantProperties.DataSourceProperties> entry : multiTenantProperties
				.getTenantAndDataSourceProperties().entrySet()) {

			String tenantKey = entry.getKey();
			MultiTenantProperties.DataSourceProperties props = entry.getValue();

			HikariDataSource ds = buildDataSource(tenantKey, props);
			targetDataSources.put(tenantKey, ds);

			if ("DEFAULT".equalsIgnoreCase(tenantKey)) {
				defaultDataSource = ds;
			}

			log.info("Registered DataSource for tenant: {}", tenantKey);
		}

		if (defaultDataSource == null) {
			throw new IllegalStateException(
					"No DEFAULT datasource configured in edue.multitenancy.tenantAndDataSourceProperties");
		}

		TenantAwareDataSource routingDataSource = new TenantAwareDataSource();
		routingDataSource.setTargetDataSources(targetDataSources);
		routingDataSource.setDefaultTargetDataSource(defaultDataSource);
		routingDataSource.afterPropertiesSet();

		return routingDataSource;
	}

	private HikariDataSource buildDataSource(String tenantKey, MultiTenantProperties.DataSourceProperties props) {
		HikariDataSource ds = new HikariDataSource();
		ds.setJdbcUrl(props.getUrl());
		ds.setDriverClassName(props.getDriverClassName());
		ds.setUsername(props.getUsername());
		ds.setPassword(props.getPassword());
		ds.setPoolName("HikariPool-" + tenantKey);
		ds.setMaximumPoolSize(10);
		ds.setMinimumIdle(2);
		ds.setIdleTimeout(30000);
		ds.setConnectionTimeout(30000);
		ds.setAutoCommit(true);
		return ds;
	}
}
