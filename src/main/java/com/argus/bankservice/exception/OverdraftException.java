package com.argus.bankservice.exception;

public class OverdraftException extends RuntimeException {
    public OverdraftException(String message) {
        super(message);
    }
}
