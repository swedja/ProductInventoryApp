package com.example.demo.exception;

import com.example.demo.dto.ErrorResponse;
import jakarta.persistence.OptimisticLockException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLockException(OptimisticLockException ex) {
        ErrorResponse errorResponse =new ErrorResponse(HttpStatus.CONFLICT.value(), "Conflict", "The entity you are trying to update has been modified by another transaction. Please refresh and try again.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}

