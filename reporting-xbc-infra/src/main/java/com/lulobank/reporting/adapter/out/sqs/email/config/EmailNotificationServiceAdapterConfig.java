package com.lulobank.reporting.adapter.out.sqs.email.config;

import com.lulobank.events.reactive.impl.SqsEventsWithTracing;
import com.lulobank.reporting.adapter.out.sqs.email.EmailNotificationServiceAdapter;
import com.lulobank.reporting.usecase.port.out.queue.EmailNotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailNotificationServiceAdapterConfig {

    @Value("${cloud.aws.sqs.queue.client-alerts-events}")
    private String clientsAlertsSqsEndpoint;

    @Bean
    public EmailNotificationService emailNotificationService(SqsEventsWithTracing sqsBraveTemplate) {
        return new EmailNotificationServiceAdapter(clientsAlertsSqsEndpoint, sqsBraveTemplate);
    }

}
