package com.argus.bankservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    private ResponseEntity<Object> handleException(RuntimeException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.badRequest()
                .body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler({DatabaseLockException.class})
    private ResponseEntity<Object> handleDatabaseBlockException(RuntimeException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.internalServerError()
                .body(Map.of("error", exception.getMessage()));
    }
}
