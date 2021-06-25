package com.lulobank.reporting.adapter.in.sqs.event;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CreateReportMessage {

    private String idClient;
    private String productType;
    private String reportType;
    private Map<String, Object> data;

    @Override
    public String toString() {
        return "CreateReportMessage{" +
                "idClient='" + idClient + '\'' +
                ", productType='" + productType + '\'' +
                ", reportType='" + reportType + '\'' +
                ", data=" + data +
                '}';
    }
}
