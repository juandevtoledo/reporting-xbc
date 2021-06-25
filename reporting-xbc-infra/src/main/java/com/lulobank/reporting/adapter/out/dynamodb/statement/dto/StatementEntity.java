package com.lulobank.reporting.adapter.out.dynamodb.statement.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.List;

@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
public class StatementEntity {

    private String idClient;
    private String productType;
    private List<StatementFileInformationEntity> listFiles;

    @DynamoDbPartitionKey
    @DynamoDbAttribute(value = "idClient")
    public String getClientId() {
        return idClient;
    }

    public void setClientId(String idClient) {
        this.idClient = idClient;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute(value = "productType")
    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    @DynamoDbAttribute(value = "listFiles")
    public List<StatementFileInformationEntity> getListFiles() {
        return listFiles;
    }

    public void setListFiles(List<StatementFileInformationEntity> listFiles) {
        this.listFiles = listFiles;
    }
}
