package com.lulobank.reporting.kernel.command.statement;

import com.lulobank.reporting.kernel.command.Command;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class CreateReport implements Command {
    private final String idClient;
    private final String productType;
    private final String reportType;
    private final Map<String, Object> data;
}

