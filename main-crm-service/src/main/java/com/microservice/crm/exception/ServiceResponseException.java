package com.microservice.crm.exception;

public class ServiceResponseException extends Exception {

	public ServiceResponseException(String message) {
		super(message);
	}

	public ServiceResponseException(String message, Throwable cause) {
		super(message, cause);
	}

}