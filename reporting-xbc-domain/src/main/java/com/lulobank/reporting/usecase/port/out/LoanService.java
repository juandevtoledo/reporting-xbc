package com.lulobank.reporting.usecase.port.out;

import com.lulobank.reporting.kernel.domain.entity.loan.LoanInformation;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.LoanId;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientIdCBS;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.PeriodDate;
import reactor.core.publisher.Mono;

public interface LoanService {

    Mono<LoanInformation> getLoanInformation(PeriodDate initialPeriod, LoanId loanId, ClientIdCBS clientIdCBS);

}
