package com.lulobank.reporting.adapter.out.dynamodb.statement.mapper;

import com.lulobank.reporting.adapter.out.dynamodb.statement.dto.StatementEntity;
import com.lulobank.reporting.adapter.out.dynamodb.statement.dto.StatementFileInformationEntity;
import com.lulobank.reporting.kernel.domain.entity.filestatement.StatementFileInformation;
import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.FileFullPath;
import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.FileName;
import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.StatementType;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientId;
import com.lulobank.reporting.kernel.domain.entity.statement.Statement;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.PeriodDate;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ProductId;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ProductType;

import java.util.List;
import java.util.stream.Collectors;

public class StatementMapper {
    private static final String SHORT_KEY_SEPARATOR = "#";

    public static Statement toStatement(StatementEntity statementEntity) {
        String[] shortKey = statementEntity.getProductType().split(SHORT_KEY_SEPARATOR);
        return Statement
                .builder()
                .clientId(ClientId.builder().value(statementEntity.getClientId()).build())
                .productType(ProductType.builder().value(shortKey[0]).build())
                .productId(ProductId.builder().value(shortKey[1]).build())
                .listFiles(toListStatementFileInformation(statementEntity.getListFiles()))
                .build();
    }

    public static StatementEntity toStatementEntity(Statement statement) {
        String shortKey = statement.getProductType().getValue().concat(SHORT_KEY_SEPARATOR).concat(statement.getProductId().getValue());
        return new StatementEntity(statement.getClientId().getValue(),
                shortKey,
                toListStatementFileInformationEntity(statement.getListFiles()));
    }


    private static List<StatementFileInformation> toListStatementFileInformation(List<StatementFileInformationEntity> list) {
        return list.stream().map(entity -> StatementFileInformation
                .builder()
                .statementType(StatementType.builder().value(entity.getStatementType()).build())
                .fileName(FileName.builder().value(entity.getFileName()).build())
                .periodDate(PeriodDate.builder().value(entity.getPeriodDate()).build())
                .fileFullPath(FileFullPath.builder().value(entity.getFileFullPath()).build())
                .installment(entity.getInstallment())
                .createdAt(entity.getCreatedAt())
                .build())
                .collect(Collectors.toList());
    }

    private static List<StatementFileInformationEntity> toListStatementFileInformationEntity(List<StatementFileInformation> list) {
        return list.stream().map(statement -> new StatementFileInformationEntity(
                statement.getFileName().getValue(),
                statement.getFileFullPath().getValue(),
                statement.getStatementType().getValue(),
                statement.getPeriodDate().getValue(),
                statement.getInstallment(),
                statement.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
