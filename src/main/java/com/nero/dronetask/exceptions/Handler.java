package com.nero.dronetask.exceptions;

import com.nero.dronetask.configs.Routes;
import com.nero.dronetask.dtos.responses.ErrorResponse;
import com.nero.dronetask.enums.ErrorType;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@RestController
public class Handler implements ErrorController {

	private static final Logger logger = LoggerFactory.getLogger(Handler.class);

	@ExceptionHandler(DroneApplicationException.class)
	public ResponseEntity<ErrorResponse> handleApplicationException(DroneApplicationException e) {
		return ResponseEntity.status(e.getErrorType().getHttpStatus()).body(new ErrorResponse(e.getMessage(), e.getErrorType(), e.getReasons()));
	}

	@ExceptionHandler(MissingRequestHeaderException.class)
	public ResponseEntity<ErrorResponse> handleError(MissingRequestHeaderException e) {
		return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), ErrorType.BAD_REQUEST, null));
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleError(HttpRequestMethodNotSupportedException e) {
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ErrorResponse(e.getMessage(), ErrorType.BAD_REQUEST,null));
	}

	@ExceptionHandler(BindException.class)
	public ResponseEntity<ErrorResponse> handleError(BindException e) {

		Map<String, String> reasons = new HashMap<>();
		e.getBindingResult().getAllErrors().parallelStream().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();

			synchronized (reasons) {
				reasons.put(fieldName, fieldName + " " + error.getDefaultMessage());
			}
		});

		return ResponseEntity.unprocessableEntity().body(new ErrorResponse(ErrorType.VALIDATION_ERROR, reasons));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleError(HttpMessageNotReadableException e) {
		String errorMessage = e.getMessage();
		logger.error(errorMessage, e);

		String[] split = errorMessage != null ? errorMessage.split(":") : null;

		return ResponseEntity.badRequest().body(new ErrorResponse((split == null || split.length == 0) ? errorMessage : split[0]));
	}

	@RequestMapping(Routes.Exception.DEFAULT_ERROR)
	public ResponseEntity<ErrorResponse> handleError(HttpServletRequest request, HttpServletResponse response) {
		HttpStatus httpStatus = getHttpStatus(request, true);
		ErrorResponse errorResponse = getErrorResponse(request, httpStatus);
		return ResponseEntity.status(httpStatus).body(errorResponse);
	}

	public static HttpStatus getHttpStatus(HttpServletRequest request, boolean defaultTo500) {

		String code = request.getParameter("code");
		Integer status = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		HttpStatus httpStatus;

		if (status != null) httpStatus = HttpStatus.valueOf(status);
		else if (code != null && code.length() > 0) httpStatus = HttpStatus.valueOf(code);
		else httpStatus = defaultTo500 ? HttpStatus.INTERNAL_SERVER_ERROR : null;

		return httpStatus;
	}

	private ErrorResponse getErrorResponse(HttpServletRequest request, HttpStatus httpStatus) {

		ErrorResponse errorResponse;

		String message = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

		switch (httpStatus) {
			case NOT_FOUND -> errorResponse = new ErrorResponse(ErrorType.RESOURCE_NOT_FOUND);
			case INTERNAL_SERVER_ERROR -> errorResponse = new ErrorResponse(ErrorType.SERVER_ERROR);
			case FORBIDDEN -> {
				if (message == null || message.equals("Forbidden"))
					errorResponse = new ErrorResponse(ErrorType.ACCESS_DENIED);
				else
					errorResponse = new ErrorResponse(message, ErrorType.ACCESS_DENIED);
			}
			case UNAUTHORIZED ->
					errorResponse = new ErrorResponse(message == null ? ErrorType.UNAUTHORIZED.getMessage() : message, ErrorType.UNAUTHORIZED);
			default ->
					errorResponse = new ErrorResponse((message != null && !message.isEmpty()) ? message : httpStatus.getReasonPhrase(), ErrorType.BAD_REQUEST);
		}

		return errorResponse;
	}
}
