package com.lulobank.reporting.adapter.out.dynamodb.statement.dynamoenhacedimp;

import com.lulobank.reporting.adapter.out.dynamodb.statement.dto.StatementEntity;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClientExtension;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class DynamoEmptyImpl implements DynamoDbAsyncTable<StatementEntity> {

    @Override
    public CompletableFuture getItem(Consumer<GetItemEnhancedRequest.Builder> requestConsumer) {
        return CompletableFuture.allOf();
    }
    @Override
    public DynamoDbAsyncIndex<StatementEntity> index(String indexName) {
        return null;
    }

    @Override
    public DynamoDbEnhancedClientExtension mapperExtension() {
        return null;
    }

    @Override
    public TableSchema<StatementEntity> tableSchema() {
        return null;
    }

    @Override
    public String tableName() {
        return null;
    }

    @Override
    public Key keyFrom(StatementEntity item) {
        return null;
    }
}
