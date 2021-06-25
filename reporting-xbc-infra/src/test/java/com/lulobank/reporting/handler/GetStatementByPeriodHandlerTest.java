package com.lulobank.reporting.handler;

import com.lulobank.reporting.adapter.in.statement.dto.StatementBodyByPeriodResponse;
import com.lulobank.reporting.adapter.in.util.GenericResponse;
import com.lulobank.reporting.kernel.command.statement.GetStatementByPeriod;
import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.FileName;
import com.lulobank.reporting.kernel.domain.entity.statement.StatementContent;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ContentBase64;
import com.lulobank.reporting.usecase.port.out.storage.error.StatementStorageException;
import com.lulobank.reporting.usecase.statement.GetStatementByPeriodUseCase;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.lulobank.reporting.utils.Sample.CURRENT_INSTALMENT;
import static com.lulobank.reporting.utils.Sample.ID_CLIENT;
import static com.lulobank.reporting.utils.Sample.ID_CREDIT_CBS;
import static com.lulobank.reporting.utils.Sample.PRODUCT_TYPE;
import static org.mockito.Mockito.when;

public class GetStatementByPeriodHandlerTest {

    @Mock
    private GetStatementByPeriodUseCase useCase;
    @Captor
    private ArgumentCaptor<GetStatementByPeriod> commandCaptor;

    private GetStatementByPeriodHandler target;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        target = new GetStatementByPeriodHandler(useCase);
    }

    @Test
    public void useCaseReturnError() {
        when(useCase.execute(commandCaptor.capture())).thenReturn(Mono.error(StatementStorageException.notFountError()));
        Mono<ResponseEntity<GenericResponse>> response = target.executeUseCase(ID_CLIENT, PRODUCT_TYPE, ID_CREDIT_CBS, CURRENT_INSTALMENT);
        StepVerifier.create(response).expectNextMatches(r -> r.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR))
                .expectComplete()
                .verifyThenAssertThat();

        Assert.assertEquals(ID_CLIENT, commandCaptor.getValue().getClientId());
        Assert.assertEquals(PRODUCT_TYPE, commandCaptor.getValue().getProductType());
        Assert.assertEquals(ID_CREDIT_CBS, commandCaptor.getValue().getProductId());
        Assert.assertEquals(CURRENT_INSTALMENT, commandCaptor.getValue().getInstallment());

    }

    @Test
    public void useCaseReturnSuccess() {
        when(useCase.execute(commandCaptor.capture())).thenReturn(Mono.just(StatementContent.builder()
                .fileName(FileName.buildFileNamePDFStatement(PRODUCT_TYPE, ID_CREDIT_CBS, LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss"))))
                .contentBase64(ContentBase64.builder().value("content").build())
                .build()));
        Mono<ResponseEntity<GenericResponse>> response = target.executeUseCase(ID_CLIENT, PRODUCT_TYPE, ID_CREDIT_CBS, CURRENT_INSTALMENT);

        StepVerifier.create(response).expectNextMatches(r -> r.getStatusCode().equals(HttpStatus.OK) &&
                r.getBody() instanceof StatementBodyByPeriodResponse)
                .expectComplete()
                .verifyThenAssertThat();

        Assert.assertEquals(ID_CLIENT, commandCaptor.getValue().getClientId());
        Assert.assertEquals(PRODUCT_TYPE, commandCaptor.getValue().getProductType());
        Assert.assertEquals(ID_CREDIT_CBS, commandCaptor.getValue().getProductId());
        Assert.assertEquals(CURRENT_INSTALMENT, commandCaptor.getValue().getInstallment());

    }
}
