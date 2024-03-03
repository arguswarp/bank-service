package com.argus.bankservice.exception;

import com.argus.bankservice.util.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    private ResponseEntity<Object> handleException(RuntimeException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({DatabaseLockException.class})
    private ResponseEntity<Object> handleDatabaseLockException(RuntimeException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.internalServerError()
                .body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler({BadCredentialsException.class})
    private ResponseEntity<Object> handleBadCredentialsException(RuntimeException exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Неверный логин или пароль"));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.badRequest()
                .body(ValidationUtils.fieldErrorsMap(exception.getBindingResult()));
    }


}
