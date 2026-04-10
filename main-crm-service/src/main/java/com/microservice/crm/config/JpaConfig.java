package com.microservice.crm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing
@EnableTransactionManagement           
@EnableJpaRepositories(basePackages = "com.microservice.crm.repository")
public class JpaConfig {
	// JPA auditing enabled — populates createdAt / updatedAt on BaseEntity
	// automatically
}