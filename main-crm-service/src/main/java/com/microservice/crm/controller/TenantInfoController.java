package com.microservice.crm.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.crm.dto.ApiResponse;
import com.microservice.crm.multitenancy.MultiTenantProperties;
import com.microservice.crm.multitenancy.TenantContextHolder;

@RestController
@RequestMapping("/tenant")
public class TenantInfoController {

	@Autowired
	private TenantContextHolder tenantContextHolder;

	@Autowired
	private MultiTenantProperties multiTenantProperties;

	/**
	 * GET /tenant/current Returns the tenant context active for this request.
	 * Useful for debugging gateway vs direct-call flows.
	 */
	@GetMapping("/current")
	public ResponseEntity<ApiResponse<Map<String, Object>>> getCurrentTenant() {
		Map<String, Object> info = new LinkedHashMap<>();
		info.put("tenantKey", tenantContextHolder.getCurrentTenantKey());
		info.put("userId", tenantContextHolder.getCurrentUserId());
		info.put("userRole", tenantContextHolder.getCurrentUserRole());
		info.put("tenantId", multiTenantProperties.getTenantKeyVsId().get(tenantContextHolder.getCurrentTenantKey()));
		return ResponseEntity.ok(ApiResponse.success(info, "Current tenant context"));
	}

	/**
	 * GET /tenant/all Returns all configured tenants (keys only, no passwords).
	 */
	@GetMapping("/all")
	public ResponseEntity<ApiResponse<Set<String>>> getAllTenants() {
		Set<String> keys = multiTenantProperties.getTenantAndDataSourceProperties().keySet();
		return ResponseEntity.ok(ApiResponse.success(keys, "Configured tenants"));
	}
}
