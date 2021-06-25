package com.lulobank.reporting.usecase.report.fatca;

import com.lulobank.reporting.kernel.command.statement.CreateReport;
import com.lulobank.reporting.kernel.domain.entity.FatcaCrsInformation;
import com.lulobank.reporting.kernel.domain.entity.digitalevidence.DigitalEvidencePageInformation;
import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.FileFullPath;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientId;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientIdCBS;
import com.lulobank.reporting.kernel.domain.entity.types.FileType;
import com.lulobank.reporting.kernel.domain.entity.vo.ContentType;
import com.lulobank.reporting.kernel.exception.BuildPdfException;
import com.lulobank.reporting.usecase.adapter.DigitalEvidenceRepository;
import com.lulobank.reporting.usecase.port.out.BuilderFileService;
import com.lulobank.reporting.usecase.port.out.SignerFileService;
import com.lulobank.reporting.usecase.port.out.queue.ClientNotificationService;
import com.lulobank.reporting.usecase.port.out.queue.EmailNotificationService;
import com.lulobank.reporting.usecase.port.out.storage.StorageService;
import com.lulobank.reporting.usecase.service.HashOutputStreamService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.when;

public class CreateFatcaReportUseCaseTest {

    private CreateFatcaReportUseCase subject;

    @Mock
    private BuilderFileService builderFileService;
    @Mock
    private SignerFileService signerFileService;
    @Mock
    private StorageService storageService;
    @Mock
    private HashOutputStreamService hashOutputStreamService;
    @Mock
    private DigitalEvidenceRepository digitalEvidenceRepository;
    @Mock
    private ClientNotificationService clientNotificationService;
    @Mock
    private EmailNotificationService emailNotificationService;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
        subject = new CreateFatcaReportUseCase(builderFileService, signerFileService, storageService,
                hashOutputStreamService, digitalEvidenceRepository, clientNotificationService, emailNotificationService);
    }

    @Test
    public void shouldExecuteUseCaseSuccess() throws IOException {

        CreateReport createReport = buildCreateReport();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        when(builderFileService.buildFile(eq(createReport.getReportType()), any())).thenReturn(Mono.just(byteArrayOutputStream));
        when(builderFileService.addDigitalEvidencePage(isA(ByteArrayOutputStream.class), isA(DigitalEvidencePageInformation.class))).thenReturn(Mono.just(byteArrayOutputStream));
        when(signerFileService.singFile(isA(ByteArrayOutputStream.class), isA(ClientIdCBS.class))).thenReturn(Mono.just(byteArrayOutputStream));
        when(storageService.saveDocumentBucket(isA(FileFullPath.class), isA(ContentType.class), isA(ByteArrayOutputStream.class), isA(Map.class), isA(FileType.class))).thenReturn(Mono.just(true));
        when(hashOutputStreamService.generateTxtByContract(isA(ByteArrayOutputStream.class))).thenReturn(Mono.just(byteArrayOutputStream));
        when(digitalEvidenceRepository.saveDigitalEvidenceDocuments(isA(ClientId.class), isA(List.class))).thenReturn(Mono.just(true));
        when(clientNotificationService.digitalEvidenceSuccessful(any()))
                .thenReturn(Mono.just(FatcaCrsInformation.builder().build()));
        when(emailNotificationService.emailNotificationFatcaDocuments(any(),any(),any(),any(),any()))
                .thenReturn(Mono.just("").then());
        Mono<Boolean> execute = subject.execute(createReport);
        StepVerifier.create(execute)
                .expectNext(true)
                .verifyComplete();
        byteArrayOutputStream.close();
    }

    @Test
    public void shouldExecuteUseCaseFailedOnBuildFile() throws IOException {

        CreateReport createReport = buildCreateReport();
        when(builderFileService.buildFile(eq(createReport.getReportType()), any())).thenReturn(Mono.error(new BuildPdfException("Error building pdf")));
        when(clientNotificationService.digitalEvidenceFailed(any()))
                .thenReturn(Mono.just(FatcaCrsInformation.builder().build()));
        Mono<Boolean> execute = subject.execute(createReport);
        StepVerifier.create(execute)
                .expectError(BuildPdfException.class)
                .verify();
    }

    @Test
    public void shouldExecuteUseCaseFailedOnDigitalEvidence() throws IOException {

        CreateReport createReport = buildCreateReport();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        when(builderFileService.buildFile(eq(createReport.getReportType()), any())).thenReturn(Mono.just(byteArrayOutputStream));
        when(builderFileService.addDigitalEvidencePage(isA(ByteArrayOutputStream.class), isA(DigitalEvidencePageInformation.class)))
                .thenReturn(Mono.error(new BuildPdfException("Error adding digital evidence")));
        when(clientNotificationService.digitalEvidenceFailed(any()))
                .thenReturn(Mono.just(FatcaCrsInformation.builder().build()));
        Mono<Boolean> execute = subject.execute(createReport);
        StepVerifier.create(execute)
                .expectError(BuildPdfException.class)
                .verify();
        byteArrayOutputStream.close();
    }

    private CreateReport buildCreateReport() {
        return CreateReport.builder()
                .idClient("idClient")
                .reportType("reportType")
                .productType("productType")
                .data(buildDataMap())
                .build();
    }

    private Map<String, Object> buildDataMap() {
        Map<String, Object> data = new HashMap<>();

        data.put("firstName","firstName");
        data.put("middleName","middleName");
        data.put("surname","surname");
        data.put("secondSurname","secondSurname");
        data.put("documentType","documentType");
        data.put("idCard","idCard");
        data.put("birthDate","18/12/1986");
        data.put("birthPlace","birthplace");
        data.put("address","address");
        data.put("addressComplement","addressComplement");
        data.put("countries", buildMapCountries());
        data.put("phoneNumber","phoneNumber");
        data.put("taxLiability","taxLiability");
        data.put("country","country");
        data.put("taxNumber","taxNumber");
        data.put("reportDate","17/11/2020T11:00:00");
        return data;
    }

    private List<Map<String, Object>> buildMapCountries(){
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();
        data.put("taxLiability", "true");
        data.put("country", "USA");
        data.put("taxNumber", "22222");
        dataList.add(data);
        return dataList;
    }
}
