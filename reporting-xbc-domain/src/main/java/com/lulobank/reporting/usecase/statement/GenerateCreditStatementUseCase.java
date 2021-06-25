package com.lulobank.reporting.usecase.statement;

import com.lulobank.reporting.kernel.command.statement.GenerateCreditStatement;
import com.lulobank.reporting.kernel.domain.entity.CreditStatementInformation;
import com.lulobank.reporting.kernel.domain.entity.filestatement.StatementFileInformation;
import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.FileFullPath;
import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.StatementType;
import com.lulobank.reporting.kernel.domain.entity.loan.CurrentPeriod;
import com.lulobank.reporting.kernel.domain.entity.loan.LastPeriod;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ProductType;
import com.lulobank.reporting.kernel.domain.entity.types.FileType;
import com.lulobank.reporting.kernel.domain.entity.loan.LoanInformation;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.LoanId;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientId;
import com.lulobank.reporting.kernel.domain.entity.statement.Statement;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.PeriodDate;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ProductId;
import com.lulobank.reporting.kernel.domain.entity.vo.ContentType;
import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.FileName;
import com.lulobank.reporting.kernel.exception.CreditStatementException;
import com.lulobank.reporting.usecase.UseCase;
import com.lulobank.reporting.usecase.port.out.BuilderFileService;
import com.lulobank.reporting.usecase.port.out.SignerFileService;
import com.lulobank.reporting.usecase.port.out.repository.StatementRepository;
import com.lulobank.reporting.usecase.port.out.storage.StorageService;
import io.vavr.collection.HashMap;
import io.vavr.control.Option;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GenerateCreditStatementUseCase implements UseCase<GenerateCreditStatement, Boolean> {

    private final BuilderFileService builderFileService;
    private final SignerFileService signerFileService;
    private final StorageService storageService;
    private final StatementRepository statementRepository;
    private final String statementLocation;
    private final String statementTransitLocation;

    private static final String FULL_NAME = "fullName";
    private static final String EMAIL = "email";
    private static final String CLIENT_ID = "clientId";
    private static final String PRODUCT_TYPE = "productType";
    private static final String FILE_NAME = "fileName";
    private static final String INSTALLMENT = "installment";
    private static final String CREATED_AT = "createdAt";

    private static final String ERROR_MESSAGE = "Error trying to generate credit statement for {0}";

    public GenerateCreditStatementUseCase(BuilderFileService builderFileService,
                                          SignerFileService signerFileService,
                                          StorageService storageService,
                                          String statementLocation,
                                          String statementTransitLocation,
                                          StatementRepository statementRepository) {

        this.builderFileService = builderFileService;
        this.signerFileService = signerFileService;
        this.storageService = storageService;
        this.statementLocation = statementLocation;
        this.statementTransitLocation = statementTransitLocation;
        this.statementRepository = statementRepository;
    }

    @Override
    public Mono<Boolean> execute(GenerateCreditStatement command) {
        LocalDateTime createdAt = LocalDateTime.now();
        FileName statementFileName = FileName.buildFileNamePDFStatement(command.getProductType(),
                command.getIdCreditCBS(), createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss")));
        String path = buildPathStatement(command.getClientId(), command.getStatementDate(), command.getIdCreditCBS(),
                command.getCurrentInstalment());
        
        return Mono.just(CreditStatementInformation
                        .buildByLoanInformationAndCommand(command))
                .flatMap(creditStatementInformation -> mapDataForStatement(creditStatementInformation)
                        .flatMap(map -> builderFileService
                                .buildFile(command.getReportType(), map))
                        .flatMap(byteArrayOutputStream -> signerFileService
                                .singFile(byteArrayOutputStream, creditStatementInformation.getClientInformation().getClientIdCBS()))
                        .flatMap(byteArrayOutputStream ->
                                saveStatementStorageService(
                                        byteArrayOutputStream,
                                        buildMetadata(creditStatementInformation, statementFileName, createdAt),
                                        statementTransitLocation,
                                        statementFileName,
                                        FileType.STATEMENT_TRANSIT
                                )
                                        .flatMap(statement ->
                                                saveStatementStorageService(
                                                        byteArrayOutputStream,
                                                        buildMetadata(creditStatementInformation, statementFileName, createdAt),
                                                        path,
                                                        statementFileName,
                                                        FileType.STATEMENT
                                                )
                                        )
                        )
                        .flatMap(storageResponse -> saveStatementStorageService(statementFileName, path,
                                creditStatementInformation, createdAt))

                )
                .filter(Objects::nonNull)
                .map(statement -> true)
                .switchIfEmpty(Mono.error(new CreditStatementException(MessageFormat.format(ERROR_MESSAGE, command.getClientId()))));
    }

    private Mono<Statement> saveStatementStorageService(FileName statementFileName, String path,
                                                        CreditStatementInformation creditStatementInformation,
                                                        LocalDateTime createdAt) {
        return statementRepository.findById(creditStatementInformation.getClientInformation().getClientId(),
                ProductType.buildProductLoan(),
                ProductId.buildProductIdFromLoanId(creditStatementInformation.getAdditionalLoanInformation().getLoanId()))
                .map(statement -> addFileToStatement(statement,
                        FileFullPath.buildFullPathByFileName(path, statementFileName),
                        creditStatementInformation.getStatementAdditionalInformation().getPeriodDate(),
                        statementFileName,
                        creditStatementInformation.getLoanInformation().getCurrentPeriod().getCurrentInstalment().getValue(),
                        createdAt))
                .flatMap(statementRepository::save)
                .switchIfEmpty(statementRepository.save(buildStatementRegistry(creditStatementInformation.getClientInformation().getClientId(),
                        creditStatementInformation.getAdditionalLoanInformation().getLoanId(),
                        FileFullPath.buildFullPathByFileName(path, statementFileName),
                        creditStatementInformation.getStatementAdditionalInformation().getPeriodDate(),
                        statementFileName,
                        creditStatementInformation.getLoanInformation().getCurrentPeriod().getCurrentInstalment().getValue(),
                        createdAt))
                );
    }

    private Mono<Boolean> saveStatementStorageService(ByteArrayOutputStream byteArrayOutputStream,
                                                      Map<String, String> metadata,
                                                      String path,
                                                      FileName statementFileName,
                                                      FileType fileType
    ) {
        return storageService
                .saveDocumentBucket(FileFullPath.buildFullPathByFileName(path, statementFileName),
                        ContentType.buildPDFContentType(),
                        byteArrayOutputStream, metadata,
                        fileType);
    }

    private Mono<Map<String, Object>> mapDataForStatement(CreditStatementInformation creditStatementInformation) {

        return Mono.fromCallable(() -> {
            LoanInformation loanInformation = creditStatementInformation.getLoanInformation();
            CurrentPeriod currentPeriod = creditStatementInformation.getLoanInformation().getCurrentPeriod();
            Option<LastPeriod> lastPeriod = creditStatementInformation.getLoanInformation().getLastPeriod();

            return HashMap.ofAll(lastPeriod.fold(Map::of, this::buildMap))
                    .put("periodDate", creditStatementInformation.getStatementAdditionalInformation().getPeriodDate().getPeriodDateWithName())
                    .put("idLoan", creditStatementInformation.getAdditionalLoanInformation().getLoanId().getValue())
                    .put("instalments", loanInformation.getTotalInstalments().getValue())
                    .put("instalment", currentPeriod.getCurrentInstalment().getValue())
                    .put("cutOffDate", currentPeriod.getCutOffDate().formatCutOffDate())
                    .put(FULL_NAME, creditStatementInformation.getClientInformation().getFullName())
                    .put("creditState", loanInformation.getLoanState().getTextInSpanish())
                    .put(EMAIL, creditStatementInformation.getClientInformation().getEmail().getValue())

                    .put("instalmentDueDate", currentPeriod.getInstalmentDueDate().formatInstalmentDueDate())
                    .put("instalmentTotalDue", currentPeriod.getInstalmentTotalDue().getValue())
                    .put("instalmentPrincipalDue", currentPeriod.getInstalmentPrincipalDue().getValue())
                    .put("instalmentInterestDue", currentPeriod.getInstalmentInterestDue().getValue())
                    .put("instalmentPenaltiesDue", currentPeriod.getInstalmentPenaltiesDue().getValue())
                    .put("inArrearsBalance", currentPeriod.getInArrearsBalance().getValue())
                    .put("feeAmount", currentPeriod.getInsuranceFee().getValue())
                    .put("legalExpenses", currentPeriod.getLegalExpenses().getValue())

                    .put("lastPeriodExist", lastPeriod.isDefined())
                    .put("isInArrears",loanInformation.getLoanState().isLoanInArrears())

                    .put("totalBalance", loanInformation.getTotalBalance().getValue())
                    .put("arrearsDays", loanInformation.getDaysInArrears().getValue())
                    .put("loanAmount", loanInformation.getLoanAmount().getValue())
                    .put("disbursementDate", loanInformation.getDisbursementDate().getDisbursementDateFormat())
                    .put("interestRate", creditStatementInformation.getAdditionalLoanInformation().getInterestRate().getValue())
                    .put("penaltyRate", loanInformation.getPenaltyRate().getValue())
                    .put("automaticDebit", setTextAutomaticDebit(creditStatementInformation.getAdditionalLoanInformation().getAutomaticDebit().getValue()))
                    .put("amortization", loanInformation.getAmortization().getValue()).mapKeys(String::valueOf).toJavaMap();
        });
    }

    private Map<String, String> buildMetadata(CreditStatementInformation creditStatementInformation, FileName fileName,
                                              LocalDateTime createdAt) {
        return Map.of(
                FULL_NAME, creditStatementInformation.getClientInformation().getFullName(),
                CLIENT_ID, creditStatementInformation.getClientInformation().getClientId().getValue(),
                EMAIL, creditStatementInformation.getClientInformation().getEmail().getValue(),
                PRODUCT_TYPE, creditStatementInformation.getStatementAdditionalInformation().getProductType().getValue(),
                FILE_NAME, fileName.getValue(),
                CREATED_AT, createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
                INSTALLMENT, creditStatementInformation.getLoanInformation().getCurrentPeriod().getCurrentInstalment()
                    .getValue().toString());
    }

    private String setTextAutomaticDebit(boolean isAutomaticDebit) {
        return isAutomaticDebit ? "Activo" : "Inactivo";
    }

    private Map<Object, Object> buildMap(LastPeriod lastPeriod) {
        return Map.of("lastTotalPaid", lastPeriod.getTotalPaid().getValue(),
                "lastPrincipalPaid", lastPeriod.getPrincipalPaid().getValue(),
                "lastInterestPaid", lastPeriod.getInterestPaid().getValue(),
                "lastPenaltyPaid", lastPeriod.getPenaltyPaid().getValue(),
                "lastFeesPaid", lastPeriod.getInsuranceFee().getValue(),
                "lastLegalExpenses", lastPeriod.getLegalExpenses().getValue());
    }

    private String buildPathStatement(String idClient, String dateStatement, String productId, Integer installment) {
        return MessageFormat.format(statementLocation, idClient, productId, installment.toString().concat("_")
                .concat(dateStatement));
    }

    private Statement buildStatementRegistry(ClientId clientId,
                                             LoanId loanId,
                                             FileFullPath fileFullPath,
                                             PeriodDate periodDate,
                                             FileName fileName,
                                             Integer installment,
                                             LocalDateTime createdAt
    ) {
        return Statement.builder()
                .clientId(clientId)
                .productId(ProductId.buildProductIdFromLoanId(loanId))
                .productType(ProductType.buildProductLoan())
                .listFiles(List.of(
                        StatementFileInformation
                                .builder()
                                .fileFullPath(fileFullPath)
                                .periodDate(periodDate)
                                .fileName(fileName)
                                .statementType(StatementType.buildLoanStatement())
                                .installment(installment)
                                .createdAt(createdAt)
                                .build()
                ))
                .build();
    }

    private Statement addFileToStatement(Statement statement,
                                         FileFullPath fileFullPath,
                                         PeriodDate periodDate,
                                         FileName fileName,
                                         Integer installment,
                                         LocalDateTime createdAt) {
        statement.getListFiles().add(StatementFileInformation
                .builder()
                .fileFullPath(fileFullPath)
                .periodDate(periodDate)
                .fileName(fileName)
                .statementType(StatementType.buildLoanStatement())
                .installment(installment)
                .createdAt(createdAt)
                .build());
        return statement;
    }
}
