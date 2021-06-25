package com.lulobank.reporting.kernel.command.statement;

import com.lulobank.reporting.kernel.command.Command;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ListPeriodsByStatementsAvailable implements Command {

    private final String clientId ;
    private final String productType;
    private final String productId;
}
