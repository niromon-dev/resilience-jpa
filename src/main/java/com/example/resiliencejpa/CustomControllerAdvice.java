package com.example.resiliencejpa;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ConnectException;

@ControllerAdvice
class CustomControllerAdvice {

    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<ErrorResponse> handleCallNotPermittedException(
            Exception e
    ) {
        return errorResponse(e, HttpStatus.SERVICE_UNAVAILABLE);
    }
    
    @ExceptionHandler(DatabaseAccessException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseAccessException(
            Exception e
    ) {
        return errorResponse(e, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleExceptions(
            Exception e
    ) {
        final HttpStatus status;
        if (e.getCause() instanceof ConnectException || e.getCause() instanceof JDBCConnectionException) {
            status = HttpStatus.SERVICE_UNAVAILABLE; // 503
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR; // 500
        }

        return errorResponse(e, status);
    }

    private ResponseEntity<ErrorResponse> errorResponse(Exception e, HttpStatus status) {
        // converting the stack trace to String
        final var stringWriter = new StringWriter();
        final var printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);

        return new ResponseEntity<>(
                new ErrorResponse(
                        status,
                        e.getMessage(),
                        stringWriter.toString()
                ),
                status
        );
    }
}