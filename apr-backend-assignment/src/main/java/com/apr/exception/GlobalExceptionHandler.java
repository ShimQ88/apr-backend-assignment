// src/main/java/com/apr/exception/GlobalExceptionHandler.java
package com.apr.exception;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus // 상태코드는 예외가 들고 있는 값 사용
    public Map<String, Object> handleRse(ResponseStatusException ex) {
        HttpStatus status = (HttpStatus) ex.getStatusCode();
        return Map.of(
                "timestamp", Instant.now().toString(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", ex.getReason()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleBadRequest(IllegalArgumentException ex) {
        return Map.of(
                "timestamp", Instant.now().toString(),
                "status", 400,
                "error", "Bad Request",
                "message", ex.getMessage()
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleConflict(IllegalStateException ex) {
        return Map.of(
                "timestamp", Instant.now().toString(),
                "status", 409,
                "error", "Conflict",
                "message", ex.getMessage()
        );
    }
}
