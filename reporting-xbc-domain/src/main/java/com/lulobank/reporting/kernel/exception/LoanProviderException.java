package com.lulobank.reporting.kernel.exception;

public class LoanProviderException extends RuntimeException{
    public LoanProviderException(String message) {
        super(message);
    }

    public LoanProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
