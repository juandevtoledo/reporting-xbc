package com.lulobank.reporting.usecase.report.fatca;

import com.lulobank.reporting.kernel.command.statement.CreateReport;
import com.lulobank.reporting.kernel.domain.entity.FatcaCrsInformation;
import com.lulobank.reporting.kernel.domain.entity.FatcaReportInformation;
import com.lulobank.reporting.kernel.domain.entity.digitalevidence.DigitalEvidencePageInformation;
import com.lulobank.reporting.kernel.domain.entity.digitalevidence.document.Document;
import com.lulobank.reporting.kernel.domain.entity.digitalevidence.document.vo.DocumentName;
import com.lulobank.reporting.kernel.domain.entity.digitalevidence.document.vo.fileinformation.FileInformation;
import com.lulobank.reporting.kernel.domain.entity.digitalevidence.document.vo.fileinformation.vo.AcceptanceTimestamp;
import com.lulobank.reporting.kernel.domain.entity.digitalevidence.document.vo.fileinformation.vo.Location;
import com.lulobank.reporting.kernel.domain.entity.fatca.FatcaInformation;
import com.lulobank.reporting.kernel.domain.entity.fatca.vo.Country;
import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.FileFullPath;
import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.FileName;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientId;
import com.lulobank.reporting.kernel.domain.entity.types.FileType;
import com.lulobank.reporting.kernel.domain.entity.vo.ContentType;
import com.lulobank.reporting.usecase.UseCase;
import com.lulobank.reporting.usecase.adapter.DigitalEvidenceRepository;
import com.lulobank.reporting.usecase.port.out.BuilderFileService;
import com.lulobank.reporting.usecase.port.out.SignerFileService;
import com.lulobank.reporting.usecase.port.out.queue.ClientNotificationService;
import com.lulobank.reporting.usecase.port.out.queue.EmailNotificationService;
import com.lulobank.reporting.usecase.port.out.storage.StorageService;
import com.lulobank.reporting.usecase.service.HashOutputStreamService;
import io.vavr.collection.HashMap;
import lombok.CustomLog;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lulobank.reporting.kernel.util.DateTimeUtil.formatToHour;
import static com.lulobank.reporting.kernel.util.DateTimeUtil.formatToShortDate;

@CustomLog
public class CreateFatcaReportUseCase implements UseCase<CreateReport, Boolean> {
    public static final String PDF_EXTENSION = "pdf";
    public static final String TXT_EXTENSION = "txt";
    public static final String FATCA = "FATCA";
    public static final String CRS = "CRS";
    public static final String USA = "USA";
    public static final String ESTADOS_UNIDOS = "Estados Unidos";

    private final BuilderFileService builderFileService;
    private final SignerFileService signerFileService;
    private final StorageService storageService;
    private final HashOutputStreamService hashOutputStreamService;
    private final DigitalEvidenceRepository digitalEvidenceRepository;
    private final ClientNotificationService clientNotificationService;
    private final EmailNotificationService emailNotificationService;

    public CreateFatcaReportUseCase(BuilderFileService builderFileService,
                                    SignerFileService signerFileService,
                                    StorageService storageService,
                                    HashOutputStreamService hashOutputStreamService,
                                    DigitalEvidenceRepository digitalEvidenceRepository,
                                    ClientNotificationService clientNotificationService,
                                    EmailNotificationService emailNotificationService) {
        this.builderFileService = builderFileService;
        this.signerFileService = signerFileService;
        this.storageService = storageService;
        this.hashOutputStreamService = hashOutputStreamService;
        this.digitalEvidenceRepository = digitalEvidenceRepository;
        this.clientNotificationService = clientNotificationService;
        this.emailNotificationService = emailNotificationService;
    }

    @Override
    public Mono<Boolean> execute(CreateReport command) {
        var fatcaReportInformation = FatcaReportInformation.buildFatcaReportInformationFromCreateReport(command);
        return Flux.fromStream(fatcaReportInformation.getFatcaInformation().stream())
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .flatMap(fatcaInformation -> signDocumentAndBuildHashDocument(command, fatcaInformation, fatcaReportInformation))
                .sequential()
                .collectList()
                .onErrorResume(e -> {
                    log.error("Error processing digital Evidence Fatca Crs. {} Cause: {}", e.getMessage(), e);
                    return clientNotificationService.digitalEvidenceFailed(buildFatcaCrsInformation(fatcaReportInformation, command))
                            .then(Mono.error(e));
                })
                .flatMap(fileFullPathList -> clientNotificationService.digitalEvidenceSuccessful(buildFatcaCrsInformation(fatcaReportInformation, command))
                        .flatMap(fatcaCrsInformation -> emailNotificationService.emailNotificationFatcaDocuments(fatcaReportInformation.getClientInformation().getClientId(),
                                fatcaReportInformation.getClientInformation().getEmail(),
                                fileFullPathList,
                                ContentType.buildPDFContentType(),
                                setDates())))
                .thenReturn(true);
    }

    private Mono<FileFullPath> signDocumentAndBuildHashDocument(CreateReport command, FatcaInformation fatcaInformation,
                                                                FatcaReportInformation fatcaReportInformation) {

        FileFullPath pdfPathPDF = getFileFullPath(command.getIdClient(), fatcaInformation.getCountry(), PDF_EXTENSION);

        return builderFileService.buildFile(command.getReportType(), buildReportMap(fatcaReportInformation, fatcaInformation))
                .doOnSuccess(report -> log.info("[CreateFatcaReportUseCase] Report created successfully"))
                .flatMap(report -> builderFileService.addDigitalEvidencePage(report, createDigitalEvidencePageInformation(fatcaReportInformation)))
                .doOnSuccess(report -> log.info("[CreateFatcaReportUseCase] page added in Digital Evidence"))
                .flatMap(report -> signerFileService.singFile(report, fatcaReportInformation.getClientInformation().getClientIdCBS()))
                .doOnSuccess(report -> log.info("[CreateFatcaReportUseCase] Report signed successfully"))
                .flatMap(report -> saveDocument(pdfPathPDF, ContentType.buildPDFContentType(), report))
                .flatMap(hashOutputStreamService::generateTxtByContract)
                .flatMap(hashes -> saveDocument(getFileFullPath(command.getIdClient(), fatcaInformation.getCountry(), TXT_EXTENSION),
                        ContentType.buildTXTContentType(), hashes))
                .flatMap(hashes -> saveDigitalEvidenceRepository(command.getIdClient(), fatcaInformation.getCountry(),
                        fatcaReportInformation.getReportInformation().getReportDate().getValue(), hashes))
                .map(ignored -> pdfPathPDF);
    }

    private Map<String, Object> buildReportMap(FatcaReportInformation fatcaReportInformation, FatcaInformation fatcaInformation) {
        return HashMap.empty()
                .put("reportDate", fatcaReportInformation.getReportInformation().getReportDate().reportDateFormat())
                .put("name", fatcaReportInformation.getClientInformation().getFirstName().getValue() + " " + fatcaReportInformation.getClientInformation().getSurname().getValue())
                .put("documentType", fatcaReportInformation.getClientInformation().getDocumentType().getValue())
                .put("document", fatcaReportInformation.getClientInformation().getCardId().getValue())
                .put("birthday", fatcaReportInformation.getClientInformation().getBirthdate().birthdateFormat())
                .put("birthPlace", fatcaReportInformation.getClientInformation().getBirthPlace().getValue())
                .put("address", fatcaReportInformation.getClientInformation().getAddress().getValue())
                .put("addressComplement", fatcaReportInformation.getClientInformation().getAddressComplement().getValue())
                .put("city", fatcaReportInformation.getClientInformation().getCity().getValue())
                .put("phoneNumber", fatcaReportInformation.getClientInformation().getPhoneNumber().getValue())
                .put("taxLiability", fatcaInformation.getTaxLiability().getTaxLiability())
                .put("country", fatcaInformation.getCountry().getValue())
                .put("taxNumber", fatcaInformation.getTaxNumber().getValue())
                .mapKeys(String::valueOf)
                .toJavaMap();
    }

    private DigitalEvidencePageInformation createDigitalEvidencePageInformation(FatcaReportInformation fatcaReportInformation) {
        return DigitalEvidencePageInformation.builder()
                .firstName(fatcaReportInformation.getClientInformation().getFirstName())
                .middleName(fatcaReportInformation.getClientInformation().getMiddleName())
                .surname(fatcaReportInformation.getClientInformation().getSurname())
                .secondSurname(fatcaReportInformation.getClientInformation().getSecondSurname())
                .cardId(fatcaReportInformation.getClientInformation().getCardId())
                .reportDate(fatcaReportInformation.getReportInformation().getReportDate())
                .build();
    }

    private Mono<ByteArrayOutputStream> saveDigitalEvidenceRepository(String idClient,
                                                                      Country country,
                                                                      LocalDateTime acceptanceTimestamp,
                                                                      ByteArrayOutputStream byteArrayOutputStream) {

        return buildDocument(FileName.buildFileNameFatca(PDF_EXTENSION, country).getValue(),
                getFileFullPath(idClient, country, PDF_EXTENSION),
                acceptanceTimestamp)
                .flatMap(document -> digitalEvidenceRepository.saveDigitalEvidenceDocuments(ClientId.builder().value(idClient).build(), document)
                        .map(done -> byteArrayOutputStream));
    }

    private Mono<List<Document>> buildDocument(String documentName,
                                               FileFullPath fileFullPath,
                                               LocalDateTime acceptanceTimestamp) {
        return Mono.just(Document.builder()
                .documentName(DocumentName.builder().value(documentName).build())
                .documentInformation(
                        buildFileInformation(acceptanceTimestamp, fileFullPath))
                .build())
                .map(List::of);
    }

    private FileInformation buildFileInformation(LocalDateTime acceptanceTimestamp,
                                                 FileFullPath fileFullPath) {
        return FileInformation.builder()
                .location(Location.builder().value(fileFullPath.getValue()).build())
                .acceptanceTimestamp(AcceptanceTimestamp.builder().value(acceptanceTimestamp).build()).build();
    }

    private FileFullPath getFileFullPath(String idClient, Country country, String extension) {
        String filePath = idClient.concat("/").concat(getTributaryType(country)).concat("/");
        var fileName = FileName.buildFileNameFatca(extension, country);
        return FileFullPath.buildFullPathByFileName(filePath, fileName);
    }

    private Mono<ByteArrayOutputStream> saveDocument(FileFullPath fileFullPath, ContentType contentType, ByteArrayOutputStream byteArrayOutputStream) {
        return storageService.saveDocumentBucket(fileFullPath, contentType, byteArrayOutputStream, Map.of(), FileType.DIGITAL_EVIDENCE)
                .map(result -> byteArrayOutputStream)
                .doOnSuccess(reportMap -> log.info(String.format("[CreateFatcaReportUseCase] File was save successful %s", fileFullPath.getValue())));
    }

    private String getTributaryType(Country country) {
        return (country.getValue().equals(USA) || country.getValue().equals(ESTADOS_UNIDOS)) ? FATCA : CRS;
    }

    private FatcaCrsInformation buildFatcaCrsInformation(FatcaReportInformation fatcaReportInformation, CreateReport command) {
        return FatcaCrsInformation.builder()
                .clientId(command.getIdClient())
                .countries(fatcaReportInformation.getFatcaInformation().stream()
                        .map(information -> Country.builder()
                                .value(information.getCountry().getValue()).build())
                        .collect(Collectors.toList()))
                .build();
    }

    private Map<String, Object> setDates() {
        LocalDateTime now = LocalDateTime.now();
        return Map.of("date", formatToShortDate(now),
                "hour", formatToHour(now));
    }

}
