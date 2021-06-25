package com.lulobank.reporting.adapter.in.sqs.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateStatementMessage {

    private String idClient;
    private String productType;
    private String reportType;
    private Map<String,String> data;

}
