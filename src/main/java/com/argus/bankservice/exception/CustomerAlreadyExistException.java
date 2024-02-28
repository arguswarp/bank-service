package com.argus.bankservice.exception;

public class CustomerAlreadyExistException extends RuntimeException {
    public CustomerAlreadyExistException(String s) {
        super(s);
    }
}
