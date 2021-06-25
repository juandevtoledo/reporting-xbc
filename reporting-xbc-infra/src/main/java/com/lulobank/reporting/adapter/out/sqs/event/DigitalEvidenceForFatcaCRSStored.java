package com.lulobank.reporting.adapter.out.sqs.event;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DigitalEvidenceForFatcaCRSStored {
    private final String clientId;
}
