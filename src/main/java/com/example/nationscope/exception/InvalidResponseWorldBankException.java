package com.example.nationscope.exception;

public class InvalidResponseWorldBankException extends RuntimeException {
    public InvalidResponseWorldBankException(String message) {
        super(message);
    }
}
