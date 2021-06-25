package com.lulobank.reporting.adapter.out.flexibility;
import com.lulobank.reporting.kernel.domain.entity.loan.LoanInformation;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.LoanId;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientIdCBS;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.PeriodDate;
import com.lulobank.reporting.kernel.exception.LoanProviderException;
import com.lulobank.reporting.utils.Sample;
import flexibility.client.connector.ProviderException;
import flexibility.client.sdk.FlexibilitySdk;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class LoanFlexibilityAdapterTest {

    @Mock
    private FlexibilitySdk flexibilitySdk;

    private LoanFlexibilityAdapter target;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        target =new LoanFlexibilityAdapter(flexibilitySdk);
    }

    @Test
    public void flexibilityReturnError() throws ProviderException {
        when(flexibilitySdk.getLoanStatement(any())).thenThrow(ProviderException.class);

        Mono<LoanInformation> response = target.getLoanInformation(
                PeriodDate.builder().value(Sample.STATEMENT_DATE).build(),
                LoanId.builder().value(Sample.ID_CREDIT_CBS).build(),
                ClientIdCBS.builder().value(Sample.ID_CLIENT_CBS).build());

        StepVerifier.create(response).expectError().verify();
    }

    @Test
    public void flexibilityReturnOk() throws  ProviderException {

        when(flexibilitySdk.getLoanStatement(any())).thenReturn(Sample.getLoanStatementResponse());

        Mono<LoanInformation> response = target.getLoanInformation(
                PeriodDate.builder().value(Sample.STATEMENT_DATE).build(),
                LoanId.builder().value(Sample.ID_CREDIT_CBS).build(),
                ClientIdCBS.builder().value(Sample.ID_CLIENT_CBS).build());

        StepVerifier.create(response).expectNextMatches(res->Sample.INSTALMENT_DUE_DATE.equals(res.getCurrentPeriod().getInstalmentDueDate().getValue())&&
                        Sample.DISBURSEMENT_DATE.equals(res.getDisbursementDate().getValue())&&
                        BigDecimal.valueOf(Sample.INSTALMENT_INTEREST_DUE).setScale(0, RoundingMode.CEILING).equals(res.getCurrentPeriod().getInstalmentInterestDue().getValue().setScale(0, RoundingMode.CEILING))&&
                        BigDecimal.valueOf(Sample.INSTALMENT_TOTAL_DUE).setScale(0, RoundingMode.CEILING).equals(res.getCurrentPeriod().getInstalmentTotalDue().getValue().setScale(0, RoundingMode.CEILING))&&
                        Sample.CURRENT_INSTALMENT.equals(res.getCurrentPeriod().getCurrentInstalment().getValue())&&
                        Sample.TOTAL_INSTALMENTS.equals(res.getTotalInstalments().getValue())&&
                        BigDecimal.valueOf(Sample.LOAN_AMOUNT).setScale(0, RoundingMode.CEILING).equals(res.getLoanAmount().getValue().setScale(0, RoundingMode.CEILING))&&
                        BigDecimal.valueOf(Sample.TOTAL_BALANCE).setScale(0, RoundingMode.CEILING).equals(res.getTotalBalance().getValue().setScale(0, RoundingMode.CEILING))
                )
                .verifyComplete();

    }
}
