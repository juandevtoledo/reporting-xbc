package com.lulobank.reporting.kernel.domain.entity.loan;

import com.lulobank.reporting.kernel.domain.entity.loan.vo.*;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AdditionalLoanInformation {

    private final LoanId loanId;
    private final AutomaticDebit automaticDebit;
    private final InterestRate interestRate;
}
