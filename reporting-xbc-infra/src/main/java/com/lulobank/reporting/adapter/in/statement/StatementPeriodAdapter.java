package com.lulobank.reporting.adapter.in.statement;

import com.lulobank.reporting.adapter.in.util.GenericResponse;
import com.lulobank.reporting.handler.GetStatementByPeriodHandler;
import com.lulobank.reporting.handler.ListPeriodsByStatementsAvailableHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/client/")
@RequiredArgsConstructor
public class StatementPeriodAdapter {


    private final ListPeriodsByStatementsAvailableHandler listPeriodsByStatementsAvailableHandler;

    private final GetStatementByPeriodHandler getStatementByPeriodHandler;

    @GetMapping(value = "{clientId}/statement/{productType}/{productId}/list-dates", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<GenericResponse>> getListPeriodsAvailable(@PathVariable("clientId") final String clientId,
                                                                         @PathVariable("productType") final String productType,
                                                                         @PathVariable("productId") final String productId) {
        return listPeriodsByStatementsAvailableHandler.executeUseCase(clientId, productType, productId);
    }

    @GetMapping(value = "{clientId}/file/{productType}/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<GenericResponse>> getStatementByPeriod(@PathVariable("clientId") final String clientId,
                                                                      @PathVariable("productType") final String productType,
                                                                      @PathVariable("productId") final String productId,
                                                                      @RequestParam("installment") final Integer installment
    ) {
        return getStatementByPeriodHandler.executeUseCase(clientId, productType, productId, installment);
    }

}
