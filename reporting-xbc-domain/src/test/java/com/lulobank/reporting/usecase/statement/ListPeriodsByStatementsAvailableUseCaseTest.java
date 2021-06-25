package com.lulobank.reporting.usecase.statement;

import com.lulobank.reporting.kernel.command.statement.ListPeriodsByStatementsAvailable;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientId;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.Period;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ProductId;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ProductType;
import com.lulobank.reporting.usecase.port.out.repository.StatementRepository;
import com.lulobank.reporting.utils.Sample;
import com.lulobank.reporting.utils.SampleStatementRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ListPeriodsByStatementsAvailableUseCaseTest {

    @Mock
    private StatementRepository statementRepository;
    @Captor
    private ArgumentCaptor<ClientId> clientIdCaptor;
    @Captor
    private ArgumentCaptor<ProductType> productTypeCaptor;
    @Captor
    private ArgumentCaptor<ProductId> productIdCaptor;

    private ListPeriodsByStatementsAvailableUseCase target;

    private ListPeriodsByStatementsAvailable command;

    @Before
    public void setup() {
        command = ListPeriodsByStatementsAvailable
                .builder()
                .productId(Sample.ID_CREDIT_CBS)
                .clientId(Sample.ID_CLIENT)
                .productType(Sample.PRODUCT_TYPE)
                .build();
        MockitoAnnotations.openMocks(this);
        target = new ListPeriodsByStatementsAvailableUseCase(statementRepository);
    }

    @Test
    public void statementNotFound() {
        when(statementRepository.findById(clientIdCaptor.capture(), productTypeCaptor.capture(), productIdCaptor.capture())).thenReturn(Mono.empty());
        Mono<List<Period>> response = target.execute(command);
        StepVerifier.create(response).expectComplete().verify();
        Assert.assertEquals(clientIdCaptor.getValue().getValue(), Sample.ID_CLIENT);
        Assert.assertEquals(productTypeCaptor.getValue().getValue(), Sample.PRODUCT_TYPE);
        Assert.assertEquals(productIdCaptor.getValue().getValue(), Sample.ID_CREDIT_CBS);
        verify(statementRepository).findById(any(), any(), any());
    }


    @Test
    public void statementFound() {
        when(statementRepository.findById(clientIdCaptor.capture(), productTypeCaptor.capture(), productIdCaptor.capture()))
                .thenReturn(Mono.just(SampleStatementRepository.getStatement()));
        Mono<List<Period>> response = target.execute(command);

        StepVerifier.create(response).expectNextMatches(s -> s.size() == 12&& s.get(0).getPeriodDate().getValue().equals(SampleStatementRepository.PERIOD_2020_11)
                && s.get(1).getPeriodDate().getValue().equals(SampleStatementRepository.PERIOD_2020_10)
                && s.get(2).getPeriodDate().getValue().equals(SampleStatementRepository.PERIOD_2020_09)
                && s.get(3).getPeriodDate().getValue().equals(SampleStatementRepository.PERIOD_2020_08)
                && s.get(4).getPeriodDate().getValue().equals(SampleStatementRepository.PERIOD_2020_07)
                && s.get(5).getPeriodDate().getValue().equals(SampleStatementRepository.PERIOD_2020_06)
                && s.get(6).getPeriodDate().getValue().equals(SampleStatementRepository.PERIOD_2020_05)
                && s.get(7).getPeriodDate().getValue().equals(SampleStatementRepository.PERIOD_2020_04)
                && s.get(8).getPeriodDate().getValue().equals(SampleStatementRepository.PERIOD_2020_03)
                && s.get(9).getPeriodDate().getValue().equals(SampleStatementRepository.PERIOD_2020_02)
                && s.get(10).getPeriodDate().getValue().equals(SampleStatementRepository.PERIOD_2020_01)
                && s.get(11).getPeriodDate().getValue().equals(SampleStatementRepository.PERIOD_2019_12)
        )
                .expectComplete()
                .verifyThenAssertThat();
        Assert.assertEquals(clientIdCaptor.getValue().getValue(), Sample.ID_CLIENT);
        Assert.assertEquals(productTypeCaptor.getValue().getValue(), Sample.PRODUCT_TYPE);
        Assert.assertEquals(productIdCaptor.getValue().getValue(), Sample.ID_CREDIT_CBS);
        verify(statementRepository).findById(any(), any(), any());
    }

    @Test
    public void statementFoundWithOneFile() {
        when(statementRepository.findById(clientIdCaptor.capture(), productTypeCaptor.capture(), productIdCaptor.capture()))
                .thenReturn(Mono.just(SampleStatementRepository.getStatementWithOneFile()));
        Mono<List<Period>> response = target.execute(command);

        StepVerifier.create(response).expectNextMatches(s -> s.size() == 1 && s.get(0).getPeriodDate().getValue().equals(SampleStatementRepository.PERIOD_2020_10))
                .expectComplete()
                .verifyThenAssertThat();
        Assert.assertEquals(clientIdCaptor.getValue().getValue(), Sample.ID_CLIENT);
        Assert.assertEquals(productTypeCaptor.getValue().getValue(), Sample.PRODUCT_TYPE);
        Assert.assertEquals(productIdCaptor.getValue().getValue(), Sample.ID_CREDIT_CBS);
        verify(statementRepository).findById(any(), any(), any());
    }

}
