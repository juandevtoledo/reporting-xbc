package com.lulobank.reporting.adapter.out.dynamodb.statement.util;

import com.lulobank.reporting.adapter.out.dynamodb.statement.dto.StatementEntity;
import com.lulobank.reporting.adapter.out.dynamodb.statement.dto.StatementFileInformationEntity;

import java.time.LocalDateTime;
import java.util.List;

public class SampleDynamo {

    private static final String idClient ="c12e7fc3-f730-4737-b476-73c41723a0ed";
    private static final String productType ="CREDIT_ACCOUNT#23246250630";
    private static final String fileFullPath ="statement/c12e7fc3-f730-4737-b476-73c41723a0ed/2020-10/23246250630/credit_account-23246250630.pdf";
    private static final String fileName ="credit_account-23246250630.pdf";
    private static final String periodDate ="2020-10";
    private static final String statementType ="CREDIT_STATEMENT";
    private static final Integer installment = 1;
    private static final LocalDateTime createdAt = LocalDateTime.of(2020, 10, 1, 0 ,0, 0);


    public static StatementEntity buildStatementEntity(){
        StatementFileInformationEntity statementFileInformationEntity= new StatementFileInformationEntity(
                fileFullPath, fileName, periodDate, statementType, installment, createdAt);
        return new StatementEntity(idClient,productType, List.of(statementFileInformationEntity));
    }

}
