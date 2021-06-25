package com.lulobank.reporting.adapter.in.sqs;

import com.lulobank.reporting.adapter.in.sqs.handler.CreateReportsHandler;
import com.lulobank.reporting.adapter.in.sqs.handler.GenerateReportsHandler;
import com.lulobank.reporting.kernel.command.statement.CreateReport;
import com.lulobank.reporting.usecase.UseCase;
import com.lulobank.reporting.usecase.statement.GenerateCreditStatementUseCase;
import org.springframework.context.annotation.Configuration;
import com.lulobank.events.reactive.EventMessage;

import java.util.Map;

@Configuration
public class SqsRegistryHandlerConfig {

    @EventMessage(name = "CreateStatementMessage")
    public GenerateReportsHandler getGenerateReportsHandler(GenerateCreditStatementUseCase generateCreditStatementUseCase) {
        return new GenerateReportsHandler(generateCreditStatementUseCase);
    }

    @EventMessage(name = "CreateReportMessage")
    public CreateReportsHandler getCreateReportsHandler(Map<String, UseCase<CreateReport, ?>> useCaseMap) {
        return new CreateReportsHandler(useCaseMap);
    }
}