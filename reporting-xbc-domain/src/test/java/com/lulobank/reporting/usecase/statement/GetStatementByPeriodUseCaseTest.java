package com.lulobank.reporting.usecase.statement;

import com.lulobank.reporting.kernel.command.statement.GetStatementByPeriod;
import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.FileFullPath;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientId;
import com.lulobank.reporting.kernel.domain.entity.statement.StatementContent;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ProductId;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ProductType;
import com.lulobank.reporting.kernel.domain.entity.types.FileType;
import com.lulobank.reporting.usecase.port.out.repository.StatementRepository;
import com.lulobank.reporting.usecase.port.out.storage.StorageService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetStatementByPeriodUseCaseTest {

    @Mock
    private StorageService storageService;
    @Mock
    private StatementRepository statementRepository;
    @Captor
    private ArgumentCaptor<ClientId> clientIdCaptor;
    @Captor
    private ArgumentCaptor<ProductType> productTypeCaptor;
    @Captor
    private ArgumentCaptor<ProductId> productIdCaptor;
    @Captor
    private ArgumentCaptor<FileFullPath> pathFileCaptor;

    private GetStatementByPeriodUseCase target;

    private GetStatementByPeriod command;

    @Before
    public void setup() {
        command = GetStatementByPeriod
                .builder()
                .productId(Sample.ID_CREDIT_CBS)
                .clientId(Sample.ID_CLIENT)
                .productType(Sample.PRODUCT_TYPE)
                .installment(Sample.CURRENT_INSTALMENT)
                .build();
        MockitoAnnotations.openMocks(this);
        target =new GetStatementByPeriodUseCase(storageService,statementRepository);

    }

    @Test
    public void statementNotFount(){
        when(statementRepository.findById(clientIdCaptor.capture(), productTypeCaptor.capture(), productIdCaptor.capture())).thenReturn(Mono.empty());
        Mono<StatementContent> response = target.execute(command);
        StepVerifier.create(response).expectComplete().verify();
        Assert.assertEquals(clientIdCaptor.getValue().getValue(), Sample.ID_CLIENT);
        Assert.assertEquals(productTypeCaptor.getValue().getValue(), Sample.PRODUCT_TYPE);
        Assert.assertEquals(productIdCaptor.getValue().getValue(), Sample.ID_CREDIT_CBS);
        verify(statementRepository).findById(any(), any(), any());
    }

    @Test
    public void statementFountButStorageServiceReturnEmpty(){
        when(statementRepository.findById(clientIdCaptor.capture(), productTypeCaptor.capture(), productIdCaptor.capture())).thenReturn(Mono.just(SampleStatementRepository.getStatement()));
        when(storageService.getFileBase64(eq(FileType.STATEMENT), pathFileCaptor.capture())).thenReturn(Mono.empty());

        Mono<StatementContent> response = target.execute(command);
        StepVerifier.create(response).expectComplete().verify();
        Assert.assertEquals(clientIdCaptor.getValue().getValue(), Sample.ID_CLIENT);
        Assert.assertEquals(productTypeCaptor.getValue().getValue(), Sample.PRODUCT_TYPE);
        Assert.assertEquals(productIdCaptor.getValue().getValue(), Sample.ID_CREDIT_CBS);
        verify(statementRepository).findById(any(), any(), any());
    }

    @Test
    public void statementFountStorageServiceReturnBase64(){
        when(statementRepository.findById(clientIdCaptor.capture(), productTypeCaptor.capture(), productIdCaptor.capture())).thenReturn(Mono.just(SampleStatementRepository.getStatement()));
        when(storageService.getFileBase64(eq(FileType.STATEMENT), pathFileCaptor.capture())).thenReturn(Mono.just("FileBase64"));

        Mono<StatementContent> response = target.execute(command);
        StepVerifier.create(response).expectNextMatches(statement-> statement.getContentBase64().getValue().equals("FileBase64")&&
                statement.getFileName().getValue().equals("credit_account-76834381467.pdf")).expectComplete().verifyThenAssertThat();
        Assert.assertEquals(clientIdCaptor.getValue().getValue(), Sample.ID_CLIENT);
        Assert.assertEquals(productTypeCaptor.getValue().getValue(), Sample.PRODUCT_TYPE);
        Assert.assertEquals(productIdCaptor.getValue().getValue(), Sample.ID_CREDIT_CBS);
        verify(statementRepository).findById(any(), any(), any());
    }
}
