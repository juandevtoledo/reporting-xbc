package com.lulobank.reporting.kernel.exception;

public class CreditStatementException extends RuntimeException{

    public CreditStatementException(String message) {
        super(message);
    }

    public CreditStatementException(String message, Throwable cause) {
        super(message, cause);
    }
}
