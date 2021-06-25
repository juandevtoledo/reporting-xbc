package com.lulobank.reporting.usecase.statement;

import com.lulobank.reporting.kernel.command.statement.GetStatementByPeriod;
import com.lulobank.reporting.kernel.domain.entity.filestatement.StatementFileInformation;
import com.lulobank.reporting.kernel.domain.entity.statement.StatementContent;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ContentBase64;
import com.lulobank.reporting.kernel.domain.entity.types.FileType;
import com.lulobank.reporting.usecase.UseCase;
import com.lulobank.reporting.usecase.port.out.repository.StatementRepository;
import com.lulobank.reporting.usecase.port.out.storage.StorageService;
import com.lulobank.reporting.usecase.util.Util;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.util.Comparator.comparing;

public class GetStatementByPeriodUseCase implements UseCase<GetStatementByPeriod, StatementContent> {

    private final StorageService storageService;
    private final StatementRepository statementRepository;

    public GetStatementByPeriodUseCase(StorageService storageService,
                                       StatementRepository statementRepository) {
        this.storageService = storageService;
        this.statementRepository = statementRepository;
    }


    @Override
    public Mono<StatementContent> execute(GetStatementByPeriod command) {
        return Util.buildRequestForStatementRepository(command.getClientId(), command.getProductType(), command.getProductId())
                .flatMap(tuple -> statementRepository.findById(tuple._1, tuple._2, tuple._3))
                .flatMap(statement -> filterPathByInstallment(statement.getListFiles(), command.getInstallment()))
                .flatMap(this::buildStatementContent);

    }

    private Mono<StatementContent> buildStatementContent(StatementFileInformation statementFileInformation){
        return storageService.getFileBase64(FileType.STATEMENT, statementFileInformation.getFileFullPath())
                .map(content -> StatementContent.builder()
                        .contentBase64(ContentBase64.builder().value(content).build())
                        .fileName(statementFileInformation.getFileName())
                        .build());
    }

    private Mono<StatementFileInformation> filterPathByInstallment(List<StatementFileInformation> statementList, Integer installment) {
        return Mono.justOrEmpty(statementList
                .stream()
                .sorted(comparing(StatementFileInformation::getCreatedAt).reversed())
                .filter(statementFileInformation -> statementFileInformation.getInstallment().equals(installment))
                .findFirst());
    }
}
