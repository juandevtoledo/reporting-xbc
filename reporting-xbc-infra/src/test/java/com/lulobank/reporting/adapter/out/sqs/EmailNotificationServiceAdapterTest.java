package com.lulobank.reporting.adapter.out.sqs;

import com.lulobank.events.reactive.api.Event;
import com.lulobank.events.reactive.impl.SqsEventsWithTracing;
import com.lulobank.reporting.adapter.out.sqs.email.EmailNotificationServiceAdapter;
import com.lulobank.reporting.adapter.out.sqs.event.EmailNotificationMessage;
import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.FileFullPath;
import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.FileName;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientId;
import com.lulobank.reporting.kernel.domain.entity.person.vo.Email;
import com.lulobank.reporting.kernel.domain.entity.vo.ContentType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

import static com.lulobank.reporting.utils.Sample.EMAIL;
import static com.lulobank.reporting.utils.Sample.ID_CLIENT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class EmailNotificationServiceAdapterTest {

    @Mock
    private SqsEventsWithTracing sqsEventsWithTracing;
    @Captor
    private ArgumentCaptor<Event<EmailNotificationMessage>> emailCaptor;

    private EmailNotificationServiceAdapter target;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
        target= new EmailNotificationServiceAdapter("",sqsEventsWithTracing);
    }

    @Test
    public void SendDigitalEvidenceSuccessfulOk() {
        when(sqsEventsWithTracing.convertAndSend(any(), emailCaptor.capture(), any()))
                .thenReturn(Mono.empty());
        Mono<Void> response = target.emailNotificationFatcaDocuments(ClientId.builder().value(ID_CLIENT).build(),
                Email.builder().value(EMAIL).build(),
                List.of(FileFullPath
                        .builder()
                        .value("path")
                        .fileName(FileName.builder().value("FileName").build()).build()),
                ContentType.buildPDFContentType(),
                Map.of());
        StepVerifier.create(response)
                .expectComplete();
        Assert.assertEquals(emailCaptor.getValue().getPayload().getClientId(), ID_CLIENT);
        Assert.assertEquals(emailCaptor.getValue().getPayload().getAttachments().size(), 1);
        Assert.assertEquals(emailCaptor.getValue().getPayload().getNotificationType(), "FATCA_CRS_DIGITAL_EVIDENCE");
        Assert.assertEquals(emailCaptor.getValue().getPayload().getTo(), EMAIL);

    }

}
