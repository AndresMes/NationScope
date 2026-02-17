package com.example.nationscope.exception;

public class DataNotAvailableForCountryWorldBankException extends RuntimeException {
    public DataNotAvailableForCountryWorldBankException(String message) {
        super(message);
    }
}
