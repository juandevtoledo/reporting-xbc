package com.lulobank.reporting.adapter.in.statement.dto;

import com.lulobank.reporting.adapter.in.util.GenericResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PeriodsByStatementAvailableResponse extends GenericResponse {

    private final List<PeriodResponse> periodList;

}
