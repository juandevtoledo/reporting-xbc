package com.lulobank.reporting.adapter.out.dynamodb.statement.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class StatementFileInformationEntity {

    private String fileName;
    private String fileFullPath;
    private String statementType;
    private String periodDate;
    private Integer installment;
    private LocalDateTime createdAt;

    @DynamoDbAttribute(value = "fileName")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @DynamoDbAttribute(value = "fileFullPath")
    public String getFileFullPath() {
        return fileFullPath;
    }

    public void setFileFullPath(String fileFullPath) {
        this.fileFullPath = fileFullPath;
    }

    @DynamoDbAttribute(value = "statementType")
    public String getStatementType() {
        return statementType;
    }

    public void setStatementType(String statementType) {
        this.statementType = statementType;
    }

    @DynamoDbAttribute(value = "periodDate")
    public String getPeriodDate() {
        return periodDate;
    }

    public void setPeriodDate(String periodDate) {
        this.periodDate = periodDate;
    }

    @DynamoDbAttribute(value = "installment")
    public Integer getInstallment() {
        return installment;
    }

    public void setInstallment(Integer installment) {
        this.installment = installment;
    }

    @DynamoDbAttribute(value = "createdAt")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
