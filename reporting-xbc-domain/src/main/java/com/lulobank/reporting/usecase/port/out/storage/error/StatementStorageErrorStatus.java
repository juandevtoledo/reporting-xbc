package com.lulobank.reporting.usecase.port.out.storage.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatementStorageErrorStatus {
    RPX_102("Couldn't connect to storage service"),
    RPX_103("Couldn't find statement in storage service"),
    ;

    private final String message;

    public static final String DEFAULT_DETAIL = "B";
}
