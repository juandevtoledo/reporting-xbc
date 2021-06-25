package com.lulobank.reporting.kernel.domain.entity.loan;

import com.lulobank.reporting.kernel.domain.entity.loan.vo.Amortization;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.DaysInArrears;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.DisbursementDate;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.LoanAmount;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.LoanState;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.PenaltyRate;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.PrincipalPaid;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.TotalBalance;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.TotalInstalments;
import io.vavr.control.Option;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoanInformation {

    private final TotalInstalments totalInstalments;
    private final CurrentPeriod currentPeriod;
    private final Option<LastPeriod> lastPeriod;
    private final TotalBalance totalBalance;
    private final PrincipalPaid principalPaid;
    private final LoanAmount loanAmount;
    private final DisbursementDate disbursementDate;
    private final PenaltyRate penaltyRate;
    private final Amortization amortization;
    private final DaysInArrears daysInArrears;
    private final LoanState loanState;

}
