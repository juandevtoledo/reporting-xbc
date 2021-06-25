package com.lulobank.reporting.adapter.out.sqs.clients.config;

import com.lulobank.events.reactive.impl.SqsEventsWithTracing;
import com.lulobank.reporting.adapter.out.sqs.clients.TributaryEvidenceMessageAdapter;
import com.lulobank.reporting.usecase.port.out.queue.ClientNotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageClientsConfig {

    @Value("${cloud.aws.sqs.queue.client-events-v2}")
    private String clientsSqsEndpoint;

    @Bean
    public ClientNotificationService getTributaryEvidenceMessageAdapter(SqsEventsWithTracing sqsBraveTemplate) {
        return new TributaryEvidenceMessageAdapter(sqsBraveTemplate, clientsSqsEndpoint);
    }

}
