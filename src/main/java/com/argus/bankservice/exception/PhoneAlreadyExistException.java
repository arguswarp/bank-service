package com.argus.bankservice.exception;

public class PhoneAlreadyExistException extends RuntimeException {
    public PhoneAlreadyExistException(String s) {
        super(s);
    }
}
