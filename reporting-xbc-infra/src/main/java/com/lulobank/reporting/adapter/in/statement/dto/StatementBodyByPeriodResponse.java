package com.lulobank.reporting.adapter.in.statement.dto;

import com.lulobank.reporting.adapter.in.util.GenericResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatementBodyByPeriodResponse extends GenericResponse {

    private final String fileName;
    private final String content;
}
