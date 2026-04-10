package com.microservice.crm.exception;

public enum ErrorCode {

	NOT_FOUND("NOT FOUND"), UNAUTHORIZED("UNAUTHORIZED USER"), INTERNAL_SERVER_ERROR("INTERNAL SERVER ERROR"),
	EXPECTATION_FAILED("ENTITY PARAMETER ERROR"), RUNTIME_EXCEPTION("Runtime Exception"), BAD_REQUEST("BAD_REQUEST");

	String message;

	ErrorCode(String message) {
		this.message = message;
	}

}
