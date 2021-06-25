package com.lulobank.reporting.kernel.domain.entity.filestatement;

import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.FileFullPath;
import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.FileName;
import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.StatementType;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.PeriodDate;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class StatementFileInformation {

    private final FileName fileName;
    private final FileFullPath fileFullPath;
    private final StatementType statementType;
    private final PeriodDate periodDate;
    private final Integer installment;
    private final LocalDateTime createdAt;

}
