package com.microservice.crm.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.crm.dto.TokenInfo;

/**
 * Hystrix / Resilience4j fallback for AuthServiceClient. If auth-service is
 * unreachable, return an invalid TokenInfo → request gets 401.
 */
@Component
public class AuthServiceClientFallback implements AuthServiceClient {

	private static final Logger log = LoggerFactory.getLogger(AuthServiceClientFallback.class);

	@Override
	public TokenInfo validateToken(String bearerToken) {
		log.error("auth-service is unavailable. Returning invalid token fallback.");
		return TokenInfo.invalid();
	}
}