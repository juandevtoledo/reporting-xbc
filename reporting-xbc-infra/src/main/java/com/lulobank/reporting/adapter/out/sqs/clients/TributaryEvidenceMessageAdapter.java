package com.lulobank.reporting.adapter.out.sqs.clients;

import com.lulobank.events.reactive.api.EventFactory;
import com.lulobank.events.reactive.impl.SqsEventsWithTracing;
import com.lulobank.reporting.adapter.out.sqs.event.DigitalEvidenceForFatcaCRSFailedToStore;
import com.lulobank.reporting.adapter.out.sqs.event.DigitalEvidenceForFatcaCRSStored;
import com.lulobank.reporting.kernel.domain.entity.FatcaCrsInformation;
import com.lulobank.reporting.usecase.port.out.queue.ClientNotificationService;
import lombok.CustomLog;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@CustomLog
public class TributaryEvidenceMessageAdapter implements ClientNotificationService {

    private final SqsEventsWithTracing sqsBraveTemplate;
    private final String clientsSqsEndpoint;

    public TributaryEvidenceMessageAdapter(SqsEventsWithTracing sqsBraveTemplate,
                                           String clientsSqsEndpoint) {
        this.sqsBraveTemplate = sqsBraveTemplate;
        this.clientsSqsEndpoint = clientsSqsEndpoint;
    }

    @Override
    public Mono<FatcaCrsInformation> digitalEvidenceSuccessful(FatcaCrsInformation payload) {
        return sqsBraveTemplate.convertAndSend(clientsSqsEndpoint,
                EventFactory
                        .ofDefaults(buildDigitalEvidenceForFatcaCRSStored(payload)).build(),
                new HashMap<>())
                .thenReturn(payload)
                .doOnError(e -> log.error("Error sending digital evidence event to clients idClient : {}", payload.getClientId()))
                .onErrorResume(e -> Mono.just(payload))
                .doOnSuccess(s-> log.info("Message to clients was send successful for :{}",payload.getClientId()));
    }

    @Override
    public Mono<FatcaCrsInformation> digitalEvidenceFailed(FatcaCrsInformation payload) {
        return sqsBraveTemplate.convertAndSend(clientsSqsEndpoint,
                EventFactory
                        .ofDefaults(buildFatcaCRSFailedToStore(payload)).build(),
                new HashMap<>())
                .thenReturn(payload)
                .doOnError(e -> log.error("Error sending digital evidence event to clients idClient : {}", payload.getClientId()))
                .onErrorResume(e -> Mono.just(payload));
    }

    private DigitalEvidenceForFatcaCRSStored buildDigitalEvidenceForFatcaCRSStored(FatcaCrsInformation payload) {
        return DigitalEvidenceForFatcaCRSStored.builder()
                .clientId(payload.getClientId()).build();
    }

    private DigitalEvidenceForFatcaCRSFailedToStore buildFatcaCRSFailedToStore(FatcaCrsInformation payload) {
        return DigitalEvidenceForFatcaCRSFailedToStore.builder()
                .clientId(payload.getClientId())
                .build();
    }

}
