package com.lulobank.reporting.adapter.out.dynamodb.statement.dynamoenhacedimp;

import com.lulobank.reporting.adapter.out.dynamodb.statement.dto.StatementEntity;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class DynamoFailImpl implements DynamoDbAsyncTable<StatementEntity> {

    @Override
    public CompletableFuture<StatementEntity> getItem(Consumer<GetItemEnhancedRequest.Builder> requestConsumer) {
        return CompletableFuture.failedFuture(new RuntimeException("Error"));
    }

    @Override
    public CompletableFuture<Void> putItem(StatementEntity requestConsumer) {
        return CompletableFuture.failedFuture(new RuntimeException("Error"));
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
