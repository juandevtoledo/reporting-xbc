package com.lulobank.reporting.kernel.exception;

public class BuildPdfException extends RuntimeException{

    public BuildPdfException(String message) {
        super(message);
    }

    public BuildPdfException(String message, Throwable cause) {
        super(message, cause);
    }
}
