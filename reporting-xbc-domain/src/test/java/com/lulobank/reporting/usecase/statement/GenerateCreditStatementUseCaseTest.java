package com.lulobank.reporting.usecase.statement;

import com.lulobank.reporting.kernel.domain.entity.statement.Statement;
import com.lulobank.reporting.kernel.exception.BuildPdfException;
import com.lulobank.reporting.kernel.exception.StorageFileException;
import com.lulobank.reporting.usecase.port.out.BuilderFileService;
import com.lulobank.reporting.usecase.port.out.SignerFileService;
import com.lulobank.reporting.usecase.port.out.repository.StatementRepository;
import com.lulobank.reporting.usecase.port.out.storage.StorageService;
import com.lulobank.reporting.utils.Sample;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.Times;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.ByteArrayOutputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GenerateCreditStatementUseCaseTest {

    private static final String STATEMENT_LOCATION ="statement/{0}/{1}/{2}/";
    private static final String STATEMENT_TRANSIT_LOCATION ="notification/pending_loan_statements/";
    @Mock
    private BuilderFileService builderFileService;
    @Mock
    private SignerFileService signerFileService;
    @Mock
    private StorageService storageService;
    @Mock
    private StatementRepository statementRepository;
    @Captor
    private ArgumentCaptor<Statement> statementCaptor;

    private GenerateCreditStatementUseCase target;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        target = new GenerateCreditStatementUseCase(builderFileService,
                signerFileService,
                storageService,
                STATEMENT_LOCATION,
                STATEMENT_TRANSIT_LOCATION,
                statementRepository);
    }

    @Test
    public void flexibilitySuccessfulBuildPDFFail() {

        when(builderFileService.buildFile(any(), any()))
                .thenReturn(Mono.error(() -> new BuildPdfException("")));
        Mono<Boolean> response = target.execute(Sample.getGenerateCreditStatement());
        StepVerifier.create(response).expectError(BuildPdfException.class).verifyThenAssertThat();
    }

    @Test
    public void flexibilitySuccessfulBuildPDFSignPDFFail() {

        when(builderFileService.buildFile(any(), any())).thenReturn(Mono.just(new ByteArrayOutputStream()));
        when(signerFileService.singFile(any(), any())).thenReturn(Mono.error(() -> new BuildPdfException("")));
        when(statementRepository.findById(any(), any(),any())).thenReturn(Mono.empty());

        Mono<Boolean> response = target.execute(Sample.getGenerateCreditStatement());
        StepVerifier.create(response).expectError(BuildPdfException.class).verifyThenAssertThat();

    }


    @Test
    public void flexibilitySuccessfulBuildPDFSignPDFS3Fail() {

        when(builderFileService.buildFile(any(), any())).thenReturn(Mono.just(new ByteArrayOutputStream()));
        when(signerFileService.singFile(any(), any())).thenReturn(Mono.just(new ByteArrayOutputStream()));
        when(statementRepository.findById(any(), any(),any())).thenReturn(Mono.empty());

        when(storageService.saveDocumentBucket(any(), any(),any(),anyMap(), any())).thenReturn(Mono.error(() -> new StorageFileException(
                "")));

        Mono<Boolean> response = target.execute(Sample.getGenerateCreditStatement());
        StepVerifier.create(response).expectError(StorageFileException.class).verifyThenAssertThat();
    }

    @Test
    public void flexibilitySuccessfulBuildPDFSignPDFS3SuccessfulStatementDoesNotExistInBD() {

        when(builderFileService.buildFile(any(), any())).thenReturn(Mono.just(new ByteArrayOutputStream()));
        when(signerFileService.singFile(any(), any())).thenReturn(Mono.just(new ByteArrayOutputStream()));
        when(statementRepository.findById(any(), any(),any())).thenReturn(Mono.empty());
        when(statementRepository.save(statementCaptor.capture())).thenReturn(Mono.just(Statement.builder().build()));
        when(storageService.saveDocumentBucket(any(), any(),any(),anyMap(), any())).thenReturn(Mono.just(true));

        Mono<Boolean> response = target.execute(Sample.getGenerateCreditStatement());

        StepVerifier.create(response).expectNext(true).expectComplete().verifyThenAssertThat();
        Assert.assertEquals(statementCaptor.getValue().getClientId().getValue(),Sample.getGenerateCreditStatement().getClientId());
        Assert.assertEquals(statementCaptor.getValue().getProductId().getValue(),Sample.getGenerateCreditStatement().getIdCreditCBS());
        Assert.assertEquals(statementCaptor.getValue().getProductType().getValue(),Sample.getGenerateCreditStatement().getProductType());
        Assert.assertEquals(statementCaptor.getValue().getListFiles().size(),1);
        verify(storageService,new Times(2)).saveDocumentBucket(any(), any(),any(),anyMap(), any());
    }

    @Test
    public void flexibilitySuccessfulBuildPDFSignPDFS3SuccessfulStatementExistInBD() {

        when(builderFileService.buildFile(any(), any())).thenReturn(Mono.just(new ByteArrayOutputStream()));
        when(signerFileService.singFile(any(), any())).thenReturn(Mono.just(new ByteArrayOutputStream()));
        when(statementRepository.findById(any(), any(),any())).thenReturn(Mono.just(Sample.getStatement()));
        when(statementRepository.save(statementCaptor.capture())).thenReturn(Mono.just(Statement.builder().build()));
        when(storageService.saveDocumentBucket( any(), any(),any(),anyMap(), any())).thenReturn(Mono.just(true));

        Mono<Boolean> response = target.execute(Sample.getGenerateCreditStatement());

        StepVerifier.create(response).expectNext(true).expectComplete().verifyThenAssertThat();
        Assert.assertEquals(statementCaptor.getValue().getClientId().getValue(),Sample.getGenerateCreditStatement().getClientId());
        Assert.assertEquals(statementCaptor.getValue().getProductId().getValue(),Sample.getGenerateCreditStatement().getIdCreditCBS());
        Assert.assertEquals(statementCaptor.getValue().getProductType().getValue(),Sample.getGenerateCreditStatement().getProductType());
        Assert.assertEquals(statementCaptor.getValue().getListFiles().size(),2);
        verify(storageService,new Times(2)).saveDocumentBucket(any(), any(),any(),anyMap(), any());

    }

}
