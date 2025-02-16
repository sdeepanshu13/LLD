package com.exception;

public class InvalidCronException extends RuntimeException {
    public InvalidCronException(String field, String value, String allowedRange) {
        super(" Error in '" + field + "': Invalid value '" + value + "'. Allowed range: " + allowedRange);
    }

    public InvalidCronException(String message) {
        super(" Error: " + message);
    }
}
