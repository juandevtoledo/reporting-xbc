package com.lulobank.reporting.handler;

import com.lulobank.reporting.adapter.in.statement.dto.StatementBodyByPeriodResponse;
import com.lulobank.reporting.adapter.in.util.GenericResponse;
import com.lulobank.reporting.handler.error.ErrorHandler;
import com.lulobank.reporting.kernel.command.statement.GetStatementByPeriod;
import com.lulobank.reporting.usecase.statement.GetStatementByPeriodUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public class GetStatementByPeriodHandler {

    private final GetStatementByPeriodUseCase useCase;


    public GetStatementByPeriodHandler(GetStatementByPeriodUseCase useCase) {
        this.useCase = useCase;
    }

    public Mono<ResponseEntity<GenericResponse>> executeUseCase(String clientId, String productType, String productId, Integer installment) {
        return useCase.execute(buildCommand(clientId, productType, productId,installment))
                .map(statementContent -> new StatementBodyByPeriodResponse(statementContent.getFileName().getValue(),
                                                                            statementContent.getContentBase64().getValue()))
                .map(response -> new ResponseEntity<GenericResponse>(response, HttpStatus.OK))
                .switchIfEmpty(Mono.just(ResponseEntity.noContent().build()))
                .onErrorResume(ErrorHandler::handleError);
    }

    private GetStatementByPeriod buildCommand(String clientId, String productType, String productId, Integer installment) {
        return GetStatementByPeriod
                .builder()
                .clientId(clientId)
                .productType(productType)
                .productId(productId)
                .installment(installment)
                .build();
    }

}
