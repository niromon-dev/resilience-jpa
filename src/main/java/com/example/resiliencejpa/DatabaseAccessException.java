package com.example.resiliencejpa;

public class DatabaseAccessException extends RuntimeException {
    public DatabaseAccessException(Throwable cause) {
        super(cause);
    }
}
