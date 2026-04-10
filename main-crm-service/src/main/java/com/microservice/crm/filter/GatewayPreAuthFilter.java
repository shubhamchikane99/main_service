package com.microservice.crm.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class GatewayPreAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String userId   = request.getHeader("X-User-Id");
            String username = request.getHeader("X-Username");
            String tenantId = request.getHeader("X-Tenant-Id");

            if (userId != null) {
                // Set SecurityContext
                UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(auth);

                // Set MDC for logging
                MDC.put("tenantId", tenantId != null ? tenantId : "unknown");
                MDC.put("userId",   userId);
                MDC.put("username", username != null ? username : "unknown");
            }

            filterChain.doFilter(request, response);

        } finally {
            // Always clear MDC after request to avoid thread leaks
            MDC.clear();
        }
    }
}