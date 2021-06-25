package com.lulobank.reporting.handler.error;

import io.vavr.collection.List;
import org.springframework.http.HttpStatus;

public class AdapterErrorMapper {

    private AdapterErrorMapper() {
    }

    public static HttpStatus getHttpStatusFromBusinessCode(String businessCode) {
        return List.of(AdapterErrorCode.values())
                .filter(inboundAdapterErrorCode -> inboundAdapterErrorCode.getBusinessCodes().contains(businessCode))
                .map(AdapterErrorCode::getHttpStatus)
                .getOrElse(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}