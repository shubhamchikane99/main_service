package com.microservice.crm.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.codec.ErrorDecoder;

@Configuration
public class FeignConfig {

	private static final Logger log = LoggerFactory.getLogger(FeignConfig.class);

	/**
	 * Custom error decoder for Feign calls to auth-service. On 401/403 from
	 * auth-service → return invalid TokenInfo rather than throwing.
	 */
	@Bean
	public ErrorDecoder errorDecoder() {
		return (methodKey, response) -> {
			log.warn("Feign error from {}: HTTP {}", methodKey, response.status());
			if (response.status() == 401 || response.status() == 403) {
				// Return exception that triggers fallback
				return new RuntimeException("Auth service rejected token: HTTP " + response.status());
			}
			return new RuntimeException("Feign call failed: " + methodKey + " HTTP " + response.status());
		};
	}
}
