package com.microservice.crm.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	// ✅ Tenant Not Found
	@ExceptionHandler(TenantNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody ServiceResponse handleTenantNotFound(TenantNotFoundException ex, HttpServletRequest request) {

		log.error("Tenant not found: {}", ex.getMessage());

		ExceptionResponse error = buildError(ex.getMessage(), request.getRequestURI());

		return ServiceResponse.asFailure(ErrorCode.BAD_REQUEST, error.getErrorMessage());
	}

	// ✅ Unauthorized
	@ExceptionHandler(UnauthorizedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public @ResponseBody ServiceResponse handleUnauthorized(UnauthorizedException ex, HttpServletRequest request) {

		log.warn("Unauthorized: {}", ex.getMessage());

		ExceptionResponse error = buildError(ex.getMessage(), request.getRequestURI());

		return ServiceResponse.asFailure(ErrorCode.UNAUTHORIZED, error.getErrorMessage());
	}

	// ✅ Resource Not Found
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public @ResponseBody ServiceResponse handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {

		log.warn("Resource not found: {}", ex.getMessage());

		ExceptionResponse error = buildError(ex.getMessage(), request.getRequestURI());

		return ServiceResponse.asFailure(ErrorCode.NOT_FOUND, error.getErrorMessage());
	}

	// ✅ Generic Exception (Fallback)
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody ServiceResponse handleGeneral(Exception ex, HttpServletRequest request) {

		log.error("Unhandled exception at {}: {}", request.getRequestURI(), ex.getMessage(), ex);

		ExceptionResponse error = buildError("Internal server error", request.getRequestURI());

		return ServiceResponse.asFailure(ErrorCode.INTERNAL_SERVER_ERROR, error.getErrorMessage());
	}
	
	
	@ExceptionHandler(DataInputValidationException.class)
	@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
	public @ResponseBody ServiceResponse handleValidationException(final DataInputValidationException exception,
			final HttpServletRequest request) {

		return ServiceResponse.asFailure(ErrorCode.EXPECTATION_FAILED, exception.getLocalizedMessage());
	}

	// ✅ Common Error Builder
	private ExceptionResponse buildError(String message, String path) {
		ExceptionResponse error = new ExceptionResponse();
		error.setErrorMessage(message);
		error.setRequestedURI(path);
		return error;
	}

}
