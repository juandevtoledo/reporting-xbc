package com.lulobank.reporting.adapter.in;

import com.lulobank.reporting.usecase.port.out.BuilderFileService;
import com.lulobank.reporting.usecase.port.out.SignerFileService;
import com.lulobank.reporting.usecase.port.out.repository.StatementRepository;
import com.lulobank.reporting.usecase.port.out.storage.StorageService;
import com.lulobank.reporting.usecase.statement.GenerateCreditStatementUseCase;
import com.lulobank.reporting.usecase.statement.GetStatementByPeriodUseCase;
import com.lulobank.reporting.usecase.statement.ListPeriodsByStatementsAvailableUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Value("${cloud.aws.s3.statement.directory}")
    private String directoryStatement;
    @Value("${cloud.aws.s3.statement-transient.directory}")
    private String directoryTransitStatement;

    @Bean
    public GenerateCreditStatementUseCase getGenerateCreditStatementUseCase(BuilderFileService builderFileService,
                                                                            SignerFileService signerFileService,
                                                                            StorageService storageService,
                                                                            StatementRepository statementRepository
                                                                            ) {
        return new GenerateCreditStatementUseCase(builderFileService, signerFileService, 
        		storageService, directoryStatement, directoryTransitStatement,statementRepository);
    }

    @Bean
    public ListPeriodsByStatementsAvailableUseCase getListStatementsAvailableUseCase(StatementRepository statementRepository) {
        return new ListPeriodsByStatementsAvailableUseCase(statementRepository);
    }

    @Bean
    public GetStatementByPeriodUseCase getStatementByPeriodUseCase(StatementRepository statementRepository,
                                                                   StorageService storageService) {
        return new GetStatementByPeriodUseCase(storageService,statementRepository);
    }
}
