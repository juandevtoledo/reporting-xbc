package com.lulobank.reporting.kernel.domain.entity.statement.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Period {

    private final Integer installment;
    private final PeriodDate periodDate;
}
