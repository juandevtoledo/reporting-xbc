package com.lulobank.reporting.kernel.domain.entity.loan;

import com.lulobank.reporting.kernel.domain.entity.loan.vo.InsuranceFee;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.InterestPaid;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.LegalExpenses;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.PenaltyPaid;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.PrincipalPaid;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.TotalPaid;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LastPeriod {

    private final TotalPaid totalPaid;
    private final PrincipalPaid principalPaid;
    private final InterestPaid interestPaid;
    private final PenaltyPaid penaltyPaid;
    private final InsuranceFee insuranceFee;
    private final LegalExpenses legalExpenses;

}
