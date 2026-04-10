package com.microservice.crm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.microservice.crm.filter.GatewayPreAuthFilter;
import com.microservice.crm.filter.TenantFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private TenantFilter tenantFilter;

	@Autowired
	private GatewayPreAuthFilter gatewayPreAuthFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(
						auth -> auth.requestMatchers("/actuator/**").permitAll().anyRequest().authenticated())
				.addFilterBefore(gatewayPreAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterAfter(tenantFilter, GatewayPreAuthFilter.class);

		return http.build();
	}
}