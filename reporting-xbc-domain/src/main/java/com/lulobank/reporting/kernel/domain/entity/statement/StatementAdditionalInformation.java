package com.lulobank.reporting.kernel.domain.entity.statement;

import com.lulobank.reporting.kernel.domain.entity.statement.vo.PeriodDate;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ProductType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StatementAdditionalInformation {

    private final PeriodDate periodDate;
    private final ProductType productType;

}
