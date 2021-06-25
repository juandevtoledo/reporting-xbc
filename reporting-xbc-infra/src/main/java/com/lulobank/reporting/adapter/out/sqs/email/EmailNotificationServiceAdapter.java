package com.lulobank.reporting.adapter.out.sqs.email;

import com.lulobank.events.reactive.api.EventFactory;
import com.lulobank.events.reactive.impl.SqsEventsWithTracing;
import com.lulobank.reporting.adapter.out.sqs.dto.EmailAttachment;
import com.lulobank.reporting.adapter.out.sqs.event.EmailNotificationMessage;
import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.FileFullPath;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientId;
import com.lulobank.reporting.kernel.domain.entity.person.vo.Email;
import com.lulobank.reporting.kernel.domain.entity.vo.ContentType;
import com.lulobank.reporting.usecase.port.out.queue.EmailNotificationService;
import lombok.CustomLog;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CustomLog
public class EmailNotificationServiceAdapter implements EmailNotificationService  {

    private final String queue;
    private final SqsEventsWithTracing sqsBraveTemplate;

    public static final String FATCA_CRS_DIGITAL_EVIDENCE = "FATCA_CRS_DIGITAL_EVIDENCE";

    public EmailNotificationServiceAdapter(String queue,
                                           SqsEventsWithTracing sqsBraveTemplate) {
        this.queue = queue;
        this.sqsBraveTemplate = sqsBraveTemplate;
    }

    @Override
    public Mono<Void> emailNotificationFatcaDocuments(ClientId clientId,
                                                      Email email,
                                                      List<FileFullPath> fileFullPath,
                                                      ContentType contentType,
                                                      Map<String, Object> dates) {
        log.info("Sending message to clients-alerts");
        return sqsBraveTemplate.convertAndSend(queue,
                EventFactory
                        .ofDefaults(buildPrepareSignedDocumentEvent(clientId,email,fileFullPath,contentType,dates)).build(),
                new HashMap<>())
                .then()
                .doOnSuccess(s -> log.error("Fatca evidence event to clients idClient was successful : {}", clientId.getValue()))
                .doOnError(e -> log.error("Error sending email with fatca evidence event to clients idClient : {}", clientId.getValue()));
    }

    private EmailNotificationMessage buildPrepareSignedDocumentEvent(ClientId clientId,
                                                                     Email email,
                                                                     List<FileFullPath> fileFullPath,
                                                                     ContentType contentType,
                                                                     Map<String, Object> dates){
        return EmailNotificationMessage
                .builder()
                .clientId(clientId.getValue())
                .to(email.getValue())
                .notificationType(FATCA_CRS_DIGITAL_EVIDENCE)
                .attachments(buildSignedDocuments(fileFullPath,contentType))
                .attributes(dates)
                .build();
    }

    private List<EmailAttachment> buildSignedDocuments(List<FileFullPath> fileFullPath,
                                                       ContentType contentType){
        return fileFullPath
                .stream()
                .map(path-> EmailAttachment
                        .builder()
                        .name(path.getFileName().getValue())
                        .url("s3://".concat(path.getValue()))
                        .content(new byte[0])
                        .contentType(contentType.getValue())
                        .build()
                )
                .collect(Collectors.toList());
    }

}
