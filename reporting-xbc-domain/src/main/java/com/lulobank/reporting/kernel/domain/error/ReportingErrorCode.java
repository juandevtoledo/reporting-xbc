package com.lulobank.reporting.kernel.domain.error;

import lombok.Getter;

@Getter
public enum ReportingErrorCode {
    PROCESS_FILE_ERROR("REP_101", "U"),
    FILE_SERVICE_ERROR("REP_102", "U"),
    DATA_BASE_ERROR("REP_103", "D"),
            ;

    private final String code;
    private final String detail;

    ReportingErrorCode(String code, String detail) {
        this.code = code;
        this.detail = detail;
    }

}
