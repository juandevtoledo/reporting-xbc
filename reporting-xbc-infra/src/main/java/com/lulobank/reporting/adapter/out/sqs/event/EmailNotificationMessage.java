package com.lulobank.reporting.adapter.out.sqs.event;

import com.lulobank.reporting.adapter.out.sqs.dto.EmailAttachment;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Builder
@Getter
public class EmailNotificationMessage {

    private final String clientId;
    private final String notificationType;
    private final String to;
    private final List<EmailAttachment> attachments;
    private final Map<String, Object> attributes;


}
