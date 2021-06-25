package com.lulobank.reporting.adapter.out.sqs.event;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DigitalEvidenceForFatcaCRSFailedToStore {
    private final String clientId;
}
