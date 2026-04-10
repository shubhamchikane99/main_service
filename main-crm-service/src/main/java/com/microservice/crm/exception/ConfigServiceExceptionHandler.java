package com.microservice.crm.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import feign.FeignException.Unauthorized;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ConfigServiceExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public @ResponseBody ServiceResponse handleResourceNotFound(final ResourceNotFoundException exception,
			final HttpServletRequest request) {

		ExceptionResponse error = new ExceptionResponse();
		error.setErrorMessage(exception.getMessage());
		// error.callerURL(request.getRequestURI());
		error.setRequestedURI(request.getRequestURI());
		return ServiceResponse.asFailure(ErrorCode.NOT_FOUND, exception.getLocalizedMessage()); 
		// return error;
	}

	@ExceptionHandler(Unauthorized.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public @ResponseBody ServiceResponse handleResourceNotFound(final Unauthorized exception,
			final HttpServletRequest request) {

		ExceptionResponse error = new ExceptionResponse();
		error.setErrorMessage(exception.getMessage());
		// error.callerURL(request.getRequestURI());
		error.setRequestedURI(request.getRequestURI());
		return ServiceResponse.asFailure(ErrorCode.UNAUTHORIZED, exception.getLocalizedMessage());
		// return error;
	}

	@ExceptionHandler(DataInputValidationException.class)
	@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
	public @ResponseBody ServiceResponse handleValidationException(final DataInputValidationException exception,
			final HttpServletRequest request) {

		return ServiceResponse.asFailure(ErrorCode.EXPECTATION_FAILED, exception.getLocalizedMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public @ResponseBody ServiceResponse handleValidationException(MethodArgumentNotValidException ex) {
		// ex.getAllErrors().stream().
		String errors = "";

		return ServiceResponse.asFailure(ErrorCode.EXPECTATION_FAILED, errors);
//        return ServiceResponse.asFailure(ErrorCode.EXPECTATION_FAILED,
//        		ex.getFieldError().getDefaultMessage());

//		return ResponseEntity.badRequest()
//            .body(ex.getBindingResult().getAllErrors().stream()
//                .map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n")));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ServiceResponse handleConstraintViolationException(ConstraintViolationException ex) {

		return ServiceResponse.asFailure(ErrorCode.EXPECTATION_FAILED, ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody ServiceResponse handleException(final Exception exception, final HttpServletRequest request) {

		return ServiceResponse.asFailure(ErrorCode.INTERNAL_SERVER_ERROR, exception.getLocalizedMessage());

	}

}
