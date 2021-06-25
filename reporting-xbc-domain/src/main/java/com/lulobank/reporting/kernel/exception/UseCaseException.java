package com.lulobank.reporting.kernel.exception;
import lombok.Getter;

@Getter
public class UseCaseException extends RuntimeException {

    private final String businessCode;
    private final String providerCode;
    private final String detail;
    public static final String UNKNOWN_DETAIL = "U";

    public UseCaseException(String businessCode, String providerCode) {
        this.businessCode = businessCode;
        this.providerCode = providerCode;
        this.detail = UNKNOWN_DETAIL;
    }

    public UseCaseException(String businessCode, String providerCode, String detail) {
        this.businessCode = businessCode;
        this.providerCode = providerCode;
        this.detail = detail;
    }

}
