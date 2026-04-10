package com.microservice.crm.config;

import feign.Logger;
import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;

import java.util.concurrent.TimeUnit;

@Configuration
public class AppConfig {

	/**
	 * Load-balanced RestTemplate for service-to-service calls via Eureka. Used as a
	 * fallback if Feign is not preferred.
	 */
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	/**
	 * Feign logger level — use BASIC in prod, FULL for debugging.
	 */
	@Bean
	public Logger.Level feignLoggerLevel() {
		return Logger.Level.BASIC;
	}

	/**
	 * Feign timeout config — auth-service validation should be fast.
	 */
	@Bean
	public Request.Options feignRequestOptions() {
		return new Request.Options(3000, TimeUnit.MILLISECONDS, // connect timeout
				5000, TimeUnit.MILLISECONDS, // read timeout
				true // follow redirects
		);
	}
}