package com.lulobank.reporting.adapter.in.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse extends GenericResponse {
    private String failure;
    private String code;
    private String detail;

}

