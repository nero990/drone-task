package com.nero.dronetask.dtos.responses;

import com.nero.dronetask.enums.ErrorType;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ErrorResponse {
	private final Error error;

	public ErrorResponse(String message) {
		this(message, ErrorType.BAD_REQUEST, new HashMap<>());
	}

	public ErrorResponse(String message, ErrorType errorType) {
		this(message, errorType, new HashMap<>());
	}

	public ErrorResponse(ErrorType errorType) {
		this(errorType, new HashMap<>());
	}

	public ErrorResponse(ErrorType errorType, Map<String, String> reasons) {
		this(errorType.getMessage(), errorType, reasons);
	}

	public ErrorResponse(String message, ErrorType errorType, Map<String, String> reasons) {
		this.error = new Error(message, errorType, reasons);
	}

	public record Error(String message, ErrorType type, Map<String, String> reasons) {
		public Error(String message, ErrorType type, Map<String, String> reasons) {
			this.message = message;
			this.type = type;
			this.reasons = reasons == null ? new HashMap<>() : reasons;
		}
	}
}
