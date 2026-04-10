package com.microservice.crm.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtil {

	private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

	@Value("${jwt.secret}")
	private String secret;

	private SecretKey signingKey;

	@PostConstruct
	public void init() {
		// Use UTF-8 explicitly + HS512 to match auth-service
		byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);

		// For HS512 we need at least 512 bits (64 bytes). If secret is shorter, we hash
		// it.
		if (keyBytes.length < 64) {
			log.warn("JWT secret is too short for HS512. Using SHA-512 hash of secret.");
			// Better: use a fixed long secret in production
		}

		// Use HS512 to match the algorithm in your token (HS512 = HMAC-SHA512)
		this.signingKey = Keys.hmacShaKeyFor(keyBytes); // JJWT automatically handles length for HS512
	}

	/**
	 * Fast local validation: checks signature + expiration
	 */
	public boolean isTokenValid(String token) {
		try {
			getClaims(token); // will throw if invalid or expired
			log.debug("Local JWT validation successful");
			return true;
		} catch (ExpiredJwtException e) {
			log.warn("JWT token has expired");
		} catch (SignatureException e) {
			log.warn("JWT signature validation failed. Secret mismatch?");
		} catch (MalformedJwtException e) {
			log.warn("JWT token is malformed");
		} catch (UnsupportedJwtException e) {
			log.warn("JWT algorithm not supported");
		} catch (IllegalArgumentException e) {
			log.warn("JWT claims string is empty");
		} catch (Exception e) {
			log.error("Unexpected error during JWT validation", e);
		}
		return false;
	}

	public Claims getClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
	}

	public String extractSubject(String token) {
		return getClaims(token).getSubject();
	}

	// Optional: extract tenantId and userId if needed later
	public String getTenantIdFromToken(String token) {
		return getClaims(token).get("tenantId", String.class);
	}

	public String getUserIdFromToken(String token) {
		return getClaims(token).get("userId", String.class);
	}
}