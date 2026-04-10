package com.microservice.crm.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

	private boolean success;
	private String message;
	private T data;
	private String tenant;
	private String timestamp;

	private ApiResponse() {
		this.timestamp = LocalDateTime.now().toString();
	}

	// ── Factory methods ───────────────────────────────────────────────────────

	public static <T> ApiResponse<T> success(T data) {
		ApiResponse<T> r = new ApiResponse<>();
		r.success = true;
		r.data = data;
		return r;
	}

	public static <T> ApiResponse<T> success(T data, String message) {
		ApiResponse<T> r = success(data);
		r.message = message;
		return r;
	}

	public static <T> ApiResponse<T> success(T data, String message, String tenant) {
		ApiResponse<T> r = success(data, message);
		r.tenant = tenant;
		return r;
	}

	public static <T> ApiResponse<T> error(String message) {
		ApiResponse<T> r = new ApiResponse<>();
		r.success = false;
		r.message = message;
		return r;
	}

	// ── Getters ───────────────────────────────────────────────────────────────

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}

	public T getData() {
		return data;
	}

	public String getTenant() {
		return tenant;
	}

	public String getTimestamp() {
		return timestamp;
	}
}