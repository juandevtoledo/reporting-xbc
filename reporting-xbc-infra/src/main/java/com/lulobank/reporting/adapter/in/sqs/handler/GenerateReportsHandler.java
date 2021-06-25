package com.lulobank.reporting.adapter.in.sqs.handler;

import com.lulobank.events.reactive.api.EventHandler;
import com.lulobank.reporting.adapter.in.sqs.event.CreateStatementMessage;
import com.lulobank.reporting.adapter.in.sqs.mapper.CreateStatementMessageToCommand;
import com.lulobank.reporting.usecase.statement.GenerateCreditStatementUseCase;
import lombok.CustomLog;
import reactor.core.publisher.Mono;

@CustomLog
public final class GenerateReportsHandler implements EventHandler<CreateStatementMessage> {

    private final GenerateCreditStatementUseCase generateCreditStatementUseCase;

    public GenerateReportsHandler(GenerateCreditStatementUseCase generateCreditStatementUseCase) {
        this.generateCreditStatementUseCase = generateCreditStatementUseCase;

    }

    @Override
    public Mono<Void> execute(CreateStatementMessage payload) {
        log.info(String.format("Creating statement for : %s", payload.getIdClient()));
        return generateCreditStatementUseCase
                .execute(CreateStatementMessageToCommand
                        .toGenerateCreditStatement(payload))
                .doOnError(error-> log.error(String.format("Error creating statement for : %s Message: %s", payload.getIdClient(),error.getMessage())))
                .doOnSuccess(r-> log.info(String.format("Statement created successful for : %s", payload.getIdClient())))
                .then();
    }

    @Override
    public Class<CreateStatementMessage> eventClass() {
        return CreateStatementMessage.class;
    }


}
