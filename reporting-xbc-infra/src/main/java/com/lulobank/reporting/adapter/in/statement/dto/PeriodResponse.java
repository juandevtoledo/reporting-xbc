package com.lulobank.reporting.adapter.in.statement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PeriodResponse {

    private final Integer installment;
    private final String period;
}
