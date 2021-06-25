package com.lulobank.reporting.adapter.in.sqs.mapper;

import com.lulobank.reporting.adapter.in.sqs.event.CreateReportMessage;
import com.lulobank.reporting.kernel.command.statement.CreateReport;

public class CreateReportMapper {

    public static CreateReport CreateReportMessageToCreateReportCommand(CreateReportMessage createReportMessage){
        return CreateReport.builder()
                .idClient(createReportMessage.getIdClient())
                .productType(createReportMessage.getProductType())
                .reportType(createReportMessage.getReportType())
                .data(createReportMessage.getData()).build();
    }
}
