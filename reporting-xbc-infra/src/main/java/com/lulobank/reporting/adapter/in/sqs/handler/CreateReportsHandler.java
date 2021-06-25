package com.lulobank.reporting.adapter.in.sqs.handler;

import com.lulobank.events.reactive.api.EventHandler;
import com.lulobank.reporting.adapter.in.sqs.event.CreateReportMessage;
import com.lulobank.reporting.adapter.in.sqs.mapper.CreateReportMapper;
import com.lulobank.reporting.kernel.command.statement.CreateReport;
import com.lulobank.reporting.usecase.UseCase;
import lombok.CustomLog;
import reactor.core.publisher.Mono;

import java.util.Map;

@CustomLog
public final class CreateReportsHandler implements EventHandler<CreateReportMessage> {

    private final Map<String, UseCase<CreateReport, ?>> useCaseMap;

    public CreateReportsHandler(Map<String, UseCase<CreateReport, ?>> useCaseMap) {
        this.useCaseMap = useCaseMap;
    }

    @Override
    public Mono<Void> execute(CreateReportMessage message) {
        log.info(String.format("Event: %s", message));

        return useCaseMap.get(message.getReportType())
                .execute(CreateReportMapper.CreateReportMessageToCreateReportCommand(message))
                .doOnError(error-> log.error(String.format("Error creating report for : %s Message: %s", message.getIdClient(),error.getMessage())))
                .doOnSuccess(r-> log.info(String.format("Report created successful for : %s", message.getIdClient())))
                .then();
    }

    @Override
    public Class<CreateReportMessage> eventClass() {
        return CreateReportMessage.class;
    }

}
