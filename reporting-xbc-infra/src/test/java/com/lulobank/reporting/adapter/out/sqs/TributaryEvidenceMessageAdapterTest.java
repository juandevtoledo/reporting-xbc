package com.lulobank.reporting.adapter.out.sqs;

import com.lulobank.events.reactive.api.Event;
import com.lulobank.events.reactive.impl.SqsEventsWithTracing;
import com.lulobank.reporting.adapter.out.sqs.clients.TributaryEvidenceMessageAdapter;
import com.lulobank.reporting.kernel.domain.entity.FatcaCrsInformation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.MessagingException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.lulobank.reporting.utils.Sample.ID_CLIENT;
import static com.lulobank.reporting.utils.Sample.getFatcaCrsInformation;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TributaryEvidenceMessageAdapterTest {

    @Mock
    private SqsEventsWithTracing sqsEventsWithTracing;

    private TributaryEvidenceMessageAdapter testedClass;



    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        testedClass = new TributaryEvidenceMessageAdapter(sqsEventsWithTracing, "Endpoint");
    }

    @Test
    public void SendDigitalEvidenceSuccessfulException() {
        when(sqsEventsWithTracing.convertAndSend(any(), any(), any()))
                .thenReturn(Mono.error(new MessagingException("Error")));
        Mono<FatcaCrsInformation> response = testedClass.digitalEvidenceSuccessful(getFatcaCrsInformation());
        StepVerifier.create(response)
                .expectNextMatches(re -> re.getClientId().equals(ID_CLIENT));
    }

    @Test
    public void SendDigitalEvidenceFailedException() {
        when(sqsEventsWithTracing.convertAndSend(any(), any(), any()))
                .thenReturn(Mono.error(new MessagingException("Error")));
        Mono<FatcaCrsInformation> response = testedClass.digitalEvidenceFailed(getFatcaCrsInformation());
        StepVerifier.create(response)
                .expectNextMatches(re -> re.getClientId().equals(ID_CLIENT));
    }

    @Test
    public void SendDigitalEvidenceSuccessfulOk() {
        when(sqsEventsWithTracing.convertAndSend(any(), any(), any()))
                .thenReturn(Mono.empty());
        Mono<FatcaCrsInformation> response = testedClass.digitalEvidenceSuccessful(getFatcaCrsInformation());
        StepVerifier.create(response)
                .expectNextMatches(re -> re.getClientId().equals(ID_CLIENT));
    }

    @Test
    public void SendDigitalEvidenceFailedOk() {
        when(sqsEventsWithTracing.convertAndSend(any(), any(), any()))
                .thenReturn(Mono.empty());
        Mono<FatcaCrsInformation> response = testedClass.digitalEvidenceFailed(getFatcaCrsInformation());
        StepVerifier.create(response)
                .expectNextMatches(re -> re.getClientId().equals(ID_CLIENT));
    }
}
