package com.lulobank.reporting.kernel.domain.entity.report;

import com.lulobank.reporting.kernel.domain.entity.report.vo.ReportDate;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReportInformation {
    private final ReportDate reportDate;
}
