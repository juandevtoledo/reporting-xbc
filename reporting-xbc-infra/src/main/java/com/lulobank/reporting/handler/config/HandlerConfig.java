package com.lulobank.reporting.handler.config;

import com.lulobank.reporting.handler.GetStatementByPeriodHandler;
import com.lulobank.reporting.handler.ListPeriodsByStatementsAvailableHandler;
import com.lulobank.reporting.usecase.statement.GetStatementByPeriodUseCase;
import com.lulobank.reporting.usecase.statement.ListPeriodsByStatementsAvailableUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerConfig {

    @Bean
    public ListPeriodsByStatementsAvailableHandler getListStatementsAvailableHandler(ListPeriodsByStatementsAvailableUseCase useCase){
        return new ListPeriodsByStatementsAvailableHandler(useCase);
    }

    @Bean
    public GetStatementByPeriodHandler getStatementByPeriodHandler(GetStatementByPeriodUseCase useCase) {
        return new GetStatementByPeriodHandler(useCase);
    }
}
