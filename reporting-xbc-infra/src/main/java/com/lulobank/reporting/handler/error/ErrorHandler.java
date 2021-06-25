package com.lulobank.reporting.handler.error;

import com.lulobank.reporting.adapter.in.util.ErrorResponse;
import com.lulobank.reporting.adapter.in.util.GenericResponse;
import com.lulobank.reporting.kernel.exception.UseCaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import static com.lulobank.reporting.handler.error.AdapterErrorMapper.getHttpStatusFromBusinessCode;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
public final class ErrorHandler {

    private ErrorHandler() {
    }

    public static Mono<ResponseEntity<GenericResponse>> handleError(final Throwable error) {


        if (error instanceof UseCaseException) {
            UseCaseException useCaseException = (UseCaseException) error;
            log.error("Use Case error , msg : {} , code : {} , detail : {} " + error.getMessage(), useCaseException.getBusinessCode(), useCaseException.getDetail());
            return buildServerErrorResponse(useCaseException);
        } else {
            log.error("Unexpected error processing a request,  msg : {} ", error.getMessage(), error);
            return Mono.just(ResponseEntity.status(INTERNAL_SERVER_ERROR).body(unExpectedError()));
        }
    }


    private static Mono<ResponseEntity<GenericResponse>> buildServerErrorResponse(final UseCaseException error) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(error.getBusinessCode())
                .failure(error.getProviderCode())
                .detail(error.getDetail())
                .build();
        return Mono.just(ResponseEntity.status(getHttpStatusFromBusinessCode(error.getProviderCode())).body(errorResponse));
    }

    private static ErrorResponse unExpectedError() {
        return ErrorResponse.builder()
                .code("RPX_500")
                .failure("-1")
                .detail(UseCaseException.UNKNOWN_DETAIL)
                .build();
    }

}