package com.microservice.crm.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.microservice.crm.config.FeignConfig;
import com.microservice.crm.dto.TokenInfo;

/**
 * Feign client calling auth-service to validate a JWT token.
 *
 * Used ONLY when a request arrives directly at CRM (bypassing gateway). When
 * request comes through gateway, X-Tenant-ID is already injected — no call
 * needed.
 *
 * name = Eureka service name (used for load-balanced lb:// resolution) url =
 * fallback for local dev without Eureka (set auth.service.url= in yml) fallback
 * = AuthServiceClientFallback — returns invalid token if auth-service is down
 * configuration = FeignConfig — custom error decoder
 */
@FeignClient(name = "auth-service", url = "${auth.service.url:http://localhost:8081}", fallback = AuthServiceClientFallback.class, configuration = FeignConfig.class)
public interface AuthServiceClient {

	/**
	 * Matches exactly with TokenValidationController
	 */
	@PostMapping("/public/token/validate") 
	TokenInfo validateToken(@RequestHeader("Authorization") String bearerToken);
}
