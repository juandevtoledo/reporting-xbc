package com.lulobank.reporting.adapter.out.sqs.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EmailAttachment {
    private final String name;
    private final String url;
    private final byte[] content;
    private final String contentType;
}
