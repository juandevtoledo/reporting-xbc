package com.lulobank.reporting.kernel.domain.entity.loan;

import com.lulobank.reporting.kernel.domain.entity.loan.vo.CurrentInstalment;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.CutOffDate;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.InArrearsBalance;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.InstalmentDueDate;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.InstalmentInterestDue;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.InstalmentPenaltiesDue;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.InstalmentPrincipalDue;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.InstalmentTotalDue;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.InsuranceFee;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.LegalExpenses;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CurrentPeriod {

    private final CutOffDate cutOffDate;
    private final InstalmentDueDate instalmentDueDate;
    private final InstalmentTotalDue instalmentTotalDue;
    private final InstalmentPrincipalDue instalmentPrincipalDue;
    private final InstalmentInterestDue instalmentInterestDue;
    private final InstalmentPenaltiesDue instalmentPenaltiesDue;
    private final InArrearsBalance inArrearsBalance;
    private final InsuranceFee insuranceFee;
    private final LegalExpenses legalExpenses;
    private final CurrentInstalment currentInstalment;

}


