package com.lulobank.reporting.handler;

import com.lulobank.reporting.adapter.in.statement.dto.PeriodsByStatementAvailableResponse;
import com.lulobank.reporting.adapter.in.util.GenericResponse;
import com.lulobank.reporting.kernel.command.statement.ListPeriodsByStatementsAvailable;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.Period;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.PeriodDate;
import com.lulobank.reporting.usecase.port.out.repository.error.StatementRepositoryException;
import com.lulobank.reporting.usecase.statement.ListPeriodsByStatementsAvailableUseCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.lulobank.reporting.utils.Sample.ID_CLIENT;
import static com.lulobank.reporting.utils.Sample.ID_CREDIT_CBS;
import static com.lulobank.reporting.utils.Sample.PRODUCT_TYPE;
import static org.mockito.Mockito.when;

public class ListPeriodsByStatementsAvailableHandlerTest {

    @Mock
    private ListPeriodsByStatementsAvailableUseCase useCase;
    @Captor
    private ArgumentCaptor<ListPeriodsByStatementsAvailable> commandCaptor;

    private static final String PERIOD="2020-10";

    private ListPeriodsByStatementsAvailableHandler target;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
        target =new ListPeriodsByStatementsAvailableHandler(useCase);
    }

    @Test
    public void useCaseReturnError(){
        when(useCase.execute(commandCaptor.capture())).thenReturn(Mono.error(StatementRepositoryException.getDefaultError()));
        Mono<ResponseEntity<GenericResponse>> response = target.executeUseCase(ID_CLIENT, PRODUCT_TYPE, ID_CREDIT_CBS);
        StepVerifier.create(response).expectNextMatches(r-> r.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR))
                .expectComplete()
                .verifyThenAssertThat();

        Assert.assertEquals(ID_CLIENT, commandCaptor.getValue().getClientId());
        Assert.assertEquals(PRODUCT_TYPE, commandCaptor.getValue().getProductType());
        Assert.assertEquals(ID_CREDIT_CBS, commandCaptor.getValue().getProductId());
    }

    @Test
    public void useCaseReturnSuccess(){
        when(useCase.execute(commandCaptor.capture())).thenReturn(Mono.just(List.of(new Period(1,
                PeriodDate.builder().value(PERIOD).build()))));
        Mono<ResponseEntity<GenericResponse>> response = target.executeUseCase(ID_CLIENT, PRODUCT_TYPE, ID_CREDIT_CBS);

        StepVerifier.create(response).expectNextMatches(r-> r.getStatusCode().equals(HttpStatus.OK)&&
        r.getBody() instanceof PeriodsByStatementAvailableResponse)
                .expectComplete()
                .verifyThenAssertThat();

        Assert.assertEquals(ID_CLIENT, commandCaptor.getValue().getClientId());
        Assert.assertEquals(PRODUCT_TYPE, commandCaptor.getValue().getProductType());
        Assert.assertEquals(ID_CREDIT_CBS, commandCaptor.getValue().getProductId());
    }

}
