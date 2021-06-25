package com.lulobank.reporting.handler.error;


import io.vavr.collection.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.lulobank.reporting.usecase.port.out.repository.error.StatementRepositoryErrorStatus.RPX_100;
import static com.lulobank.reporting.usecase.port.out.repository.error.StatementRepositoryErrorStatus.RPX_101;

@Getter
@AllArgsConstructor
public enum AdapterErrorCode {

    NOT_FOUND(List.of(RPX_101.name()), HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR(List.of(RPX_100.name()), HttpStatus.INTERNAL_SERVER_ERROR);

    private final List<String> businessCodes;
    private final HttpStatus httpStatus;
}