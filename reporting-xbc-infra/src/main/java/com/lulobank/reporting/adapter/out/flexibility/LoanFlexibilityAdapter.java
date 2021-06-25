package com.lulobank.reporting.adapter.out.flexibility;

import com.lulobank.reporting.kernel.domain.entity.loan.CurrentPeriod;
import com.lulobank.reporting.kernel.domain.entity.loan.LastPeriod;
import com.lulobank.reporting.kernel.domain.entity.loan.LoanInformation;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.Amortization;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.CurrentInstalment;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.CutOffDate;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.DaysInArrears;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.DisbursementDate;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.InArrearsBalance;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.InstalmentDueDate;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.InstalmentInterestDue;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.InstalmentPrincipalDue;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.InstalmentTotalDue;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.InsuranceFee;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.InterestPaid;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.LegalExpenses;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.LoanAmount;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.LoanId;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.LoanState;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.PenaltyPaid;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.PenaltyRate;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.PrincipalPaid;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.TotalBalance;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.TotalInstalments;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.TotalPaid;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientIdCBS;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.PeriodDate;
import com.lulobank.reporting.kernel.exception.LoanProviderException;
import com.lulobank.reporting.usecase.port.out.LoanService;
import flexibility.client.models.request.GetLoanStatementRequest;
import flexibility.client.models.response.GetLoanStatementResponse;
import flexibility.client.sdk.FlexibilitySdk;
import io.vavr.control.Option;
import lombok.CustomLog;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Objects;

@CustomLog
public class LoanFlexibilityAdapter implements LoanService {

    private final FlexibilitySdk flexibilitySdk;
    private static final String ERROR_MESSAGE = "Error trying to get loan information for {0}";
    private static final String ERROR_MESSAGE_CASTING_LOAN_INFORMATION = "Error creating value objet loan information {0}";

    public LoanFlexibilityAdapter(FlexibilitySdk flexibilitySdk) {
        this.flexibilitySdk = flexibilitySdk;
    }

    @Override
    public Mono<LoanInformation> getLoanInformation(PeriodDate initialPeriod, LoanId loanId, ClientIdCBS clientIdCBS) {

        return Mono.fromCallable(() -> flexibilitySdk.getLoanStatement(getLoanStatementRequest(initialPeriod.getValue(), loanId.getValue(), clientIdCBS.getValue())))
                .publishOn(Schedulers.elastic())
                .filter(Objects::nonNull)
                .flatMap(this::buildLoanInformation)
                .onErrorResume(e -> Mono.error(new LoanProviderException(MessageFormat.format(ERROR_MESSAGE,clientIdCBS.getValue()))))
                .switchIfEmpty(Mono.error(new LoanProviderException(MessageFormat.format(ERROR_MESSAGE,clientIdCBS.getValue()))))
                .doOnError(e -> log.error(e.getMessage()));
    }

    private GetLoanStatementRequest getLoanStatementRequest(String initialPeriod, String idProduct, String idCbs) {
        GetLoanStatementRequest loanStatement = new GetLoanStatementRequest();
        loanStatement.setClientId(idCbs);
        loanStatement.setLoanNumber(idProduct);
        loanStatement.setStatementDate(initialPeriod);
        return loanStatement;
    }

    private Mono<LoanInformation> buildLoanInformation(GetLoanStatementResponse loanInformation) {
        GetLoanStatementResponse.LoanData currentPeriod = loanInformation.getLoanData();
        Option<GetLoanStatementResponse.LoanData> lastPeriod = Option.of(loanInformation.getPreviousLoanPeriodData());

        return Mono.fromCallable(()-> LoanInformation.builder()
                .totalInstalments(TotalInstalments.builder().value(loanInformation.getInstalments()).build())
                .currentPeriod(buildCurrentPeriod(currentPeriod,loanInformation.getInArrearsBalance()))
                .lastPeriod(buildLastPeriod(lastPeriod))
                .totalBalance(TotalBalance
                        .builder().value(doubleToBigDecimal(loanInformation.getTotalBalance()))
                        .build())
                .principalPaid(PrincipalPaid.builder().value(doubleToBigDecimal(loanInformation.getPrincipalPaid())).build())
                .loanAmount(LoanAmount
                        .builder().value(doubleToBigDecimal(loanInformation.getLoanAmount()))
                        .build())
                .disbursementDate(DisbursementDate.builder().value(loanInformation.getDisbursementDate()).build())
                .penaltyRate(PenaltyRate.builder().value(doubleToBigDecimal(loanInformation.getPenaltyRate())).build())
                .amortization(Amortization.builder().value(loanInformation.getAmortization()).build())
                .daysInArrears(DaysInArrears.builder().value(loanInformation.getDaysInArrears().intValue()).build())
                .loanState(LoanState.builder().value(loanInformation.getState().getValue()).build())
                .build())
                .doOnError(e -> log.error(ERROR_MESSAGE_CASTING_LOAN_INFORMATION,e.getMessage()));
    }

    private CurrentPeriod buildCurrentPeriod( GetLoanStatementResponse.LoanData currentPeriod,Double inArrearsBalance){

        return CurrentPeriod.builder()
                .cutOffDate(CutOffDate.builder().value(currentPeriod.getCutOffDate()).build())
                .instalmentDueDate(InstalmentDueDate.builder().value(currentPeriod.getInstalmentDueDate()).build())
                .instalmentTotalDue(InstalmentTotalDue.builder()
                        .value(doubleToBigDecimal(currentPeriod.getInstalmentTotalDue()))
                        .build())
                .instalmentPrincipalDue(InstalmentPrincipalDue.builder()
                        .value(doubleToBigDecimal(currentPeriod.getInstalmentPrincipalDue()))
                        .build())
                .instalmentInterestDue(InstalmentInterestDue.builder()
                        .value(doubleToBigDecimal(currentPeriod.getInstalmentInterestDue()))
                        .build())
                .inArrearsBalance(InArrearsBalance.builder().value(doubleToBigDecimal(inArrearsBalance)).build())
                .currentInstalment(CurrentInstalment.builder().value(currentPeriod.getInstalment()).build())
                .insuranceFee(InsuranceFee.builder().value(doubleToBigDecimal(currentPeriod.getFeesAmount())).build())
                .legalExpenses(LegalExpenses.builder().value(doubleToBigDecimal(currentPeriod.getLegalExpenses())).build())
                .build();
    }

    private Option<LastPeriod> buildLastPeriod(Option<GetLoanStatementResponse.LoanData> lastPeriod){

        return lastPeriod
                .map(loanData -> LastPeriod.builder()
                    .totalPaid(TotalPaid.builder().value(doubleToBigDecimal(loanData.getTotalPaid())).build())
                    .principalPaid(PrincipalPaid.builder().value(doubleToBigDecimal(loanData.getPrincipalPaid())).build())
                    .interestPaid(InterestPaid.builder().value(doubleToBigDecimal(loanData.getInterestPaid())).build())
                    .penaltyPaid(PenaltyPaid.builder().value(doubleToBigDecimal(loanData.getPenaltyPaid())).build())
                    .insuranceFee(InsuranceFee.builder().value(doubleToBigDecimal(loanData.getFeesPaid())).build())
                    .legalExpenses(LegalExpenses.builder().value(doubleToBigDecimal(loanData.getLegalExpenses())).build())
                    .build());

    }

    private BigDecimal doubleToBigDecimal(final Double value) {
        return new BigDecimal(String.valueOf(value));
    }

}
