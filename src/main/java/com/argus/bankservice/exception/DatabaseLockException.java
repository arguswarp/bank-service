package com.argus.bankservice.exception;

public class DatabaseLockException extends RuntimeException {
    public DatabaseLockException(String message) {
        super(message);
    }
}
