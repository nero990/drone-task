package com.nero.dronetask.enums;

import org.springframework.http.HttpStatus;

public enum ErrorType {
	SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Oops! Something went wrong! Help us improve your experience by sending an error report"),
	VALIDATION_ERROR(HttpStatus.UNPROCESSABLE_ENTITY, "The given data was invalid."),
	RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not found."),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthenticated."),
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access denied! You don't have the necessary privilege to access this resource."),
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "Supplied input not valid."),
	GENERIC(HttpStatus.BAD_REQUEST, "An error occurred. Please try again later.");

	final HttpStatus httpStatus;
	final String message;

	ErrorType(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public String getMessage() {
		return message;
	}
}
