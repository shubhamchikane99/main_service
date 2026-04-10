package com.microservice.crm.filter;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.microservice.crm.client.AuthServiceClient;
import com.microservice.crm.dto.TokenInfo;
import com.microservice.crm.multitenancy.MultiTenantProperties;
import com.microservice.crm.multitenancy.TenantContext;
import com.microservice.crm.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(1)
public class TenantFilter extends OncePerRequestFilter {

	private static final Logger log = LoggerFactory.getLogger(TenantFilter.class);

	private static final String HEADER_TENANT_ID = "X-Tenant-Id";
	private static final String HEADER_USER_ID = "X-User-Id";
	private static final String HEADER_USER_ROLE = "X-User-Role";
	private static final String HEADER_AUTH = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthServiceClient authServiceClient;

	@Autowired
	private MultiTenantProperties multiTenantProperties;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String tenantHeader = request.getHeader(HEADER_TENANT_ID);

			if (tenantHeader != null && !tenantHeader.isBlank()) {
				// ── PATH A: Request came through API Gateway ──────────────
				String userId = request.getHeader(HEADER_USER_ID);
				String role = request.getHeader(HEADER_USER_ROLE);
				String resolvedKey = multiTenantProperties.resolveKeyFromId(tenantHeader);

				log.info("PATH A — tenantHeader={}, resolvedKey={}, userId={}, role={}", tenantHeader, resolvedKey,
						userId, role);

				MDC.put("tenantId", resolvedKey != null ? resolvedKey : "unknown");
				MDC.put("userId", userId != null ? userId : "unknown");

				TenantContext.set(resolvedKey, userId, role);
				setSecurityContext(userId, role);

			} else {
				// ── PATH B: Direct call — validate via auth-service ───────
				log.info("PATH B — no X-Tenant-Id header, validating via auth-service");

				String authHeader = request.getHeader(HEADER_AUTH);
				if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
					sendUnauthorized(response, "Missing Authorization header");
					return;
				}

				TokenInfo tokenInfo;
				try {
					tokenInfo = authServiceClient.validateToken(authHeader);
				} catch (Exception e) {
					log.error("Failed to reach auth-service for token validation", e);
					sendUnauthorized(response, "Auth service unavailable");
					return;
				}

				if (tokenInfo == null || !tokenInfo.isValid()) {
					sendUnauthorized(response, "Token invalid or revoked");
					return;
				}

				String rawToken = authHeader.substring(7);
				String tenantId = jwtUtil.getTenantIdFromToken(rawToken);
				String userId = jwtUtil.getUserIdFromToken(rawToken);
				String resolvedKey = multiTenantProperties.resolveKeyFromId(tenantId);

				log.info("PATH B — tenantId={}, resolvedKey={}, userId={}", tenantId, resolvedKey, userId);

				MDC.put("tenantId", resolvedKey != null ? resolvedKey : "unknown");
				MDC.put("userId", userId != null ? userId : "unknown");

				TenantContext.set(resolvedKey, userId, null);
				setSecurityContext(userId, null);
			}

			filterChain.doFilter(request, response);

		} finally {
			TenantContext.clear();
			SecurityContextHolder.clearContext();
			MDC.clear();
		}
	}

	private void setSecurityContext(String userId, String role) {
		List<SimpleGrantedAuthority> authorities = (role != null && !role.isBlank())
				? List.of(new SimpleGrantedAuthority("ROLE_" + role))
				: List.of();

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null,
				authorities);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		log.debug("SecurityContext set for userId={}, role={}", userId, role);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return request.getRequestURI().startsWith("/actuator");
	}

	private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
		log.warn("Unauthorized: {}", message);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json");
		response.getWriter().write("{\"error\":\"UNAUTHORIZED\",\"message\":\"" + message + "\"}");
	}
}