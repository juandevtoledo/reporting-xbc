package com.lulobank.reporting.usecase.port.out.repository.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatementRepositoryErrorStatus {

    RPX_100("Couldn't connect to repository"),
    RPX_101("Couldn't find statement"),
    ;

    private final String message;

    public static final String DEFAULT_DETAIL = "D";
}
