package com.lulobank.reporting.handler;

import com.lulobank.reporting.adapter.in.statement.dto.PeriodResponse;
import com.lulobank.reporting.adapter.in.statement.dto.PeriodsByStatementAvailableResponse;
import com.lulobank.reporting.adapter.in.util.GenericResponse;
import com.lulobank.reporting.handler.error.ErrorHandler;
import com.lulobank.reporting.kernel.command.statement.ListPeriodsByStatementsAvailable;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.Period;
import com.lulobank.reporting.usecase.statement.ListPeriodsByStatementsAvailableUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

public class ListPeriodsByStatementsAvailableHandler {

    private final ListPeriodsByStatementsAvailableUseCase useCase;


    public ListPeriodsByStatementsAvailableHandler(ListPeriodsByStatementsAvailableUseCase useCase) {
        this.useCase = useCase;
    }

    public Mono<ResponseEntity<GenericResponse>> executeUseCase(String clientId,String productType,String productId){
        return useCase.execute(buildCommand(clientId, productType, productId))
                .map(this::convertToPeriod)
                .map(PeriodsByStatementAvailableResponse::new)
                .map(response -> new ResponseEntity<GenericResponse>(response,HttpStatus.OK))
                .switchIfEmpty(Mono.just(ResponseEntity.noContent().build()))
                .onErrorResume(ErrorHandler::handleError);
    }

    private ListPeriodsByStatementsAvailable buildCommand(String clientId, String productType, String productId) {
        return ListPeriodsByStatementsAvailable.builder().clientId(clientId).productType(productType).productId(productId).build();
    }

    private List<PeriodResponse> convertToPeriod(List<Period> list ){
        return list.stream().map(period -> new PeriodResponse(period.getInstallment(),
                    period.getPeriodDate().getValue()))
                .collect(Collectors.toList());
    }
}
