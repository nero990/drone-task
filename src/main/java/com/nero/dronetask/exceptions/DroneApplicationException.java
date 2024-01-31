package com.nero.dronetask.exceptions;

import com.nero.dronetask.enums.ErrorType;
import lombok.Getter;

import java.util.Map;

@Getter
public class DroneApplicationException extends RuntimeException {
    private final ErrorType errorType;
    private Map<String, String> reasons;

    public DroneApplicationException(String message) {
        super(message);
        this.errorType = ErrorType.BAD_REQUEST;
    }

    public DroneApplicationException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public DroneApplicationException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }
}
