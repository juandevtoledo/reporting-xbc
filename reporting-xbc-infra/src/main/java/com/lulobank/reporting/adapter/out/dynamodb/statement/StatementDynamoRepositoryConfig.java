package com.lulobank.reporting.adapter.out.dynamodb.statement;

import com.lulobank.reporting.usecase.port.out.repository.StatementRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

@Configuration
public class StatementDynamoRepositoryConfig {

    @Bean
    public StatementRepository getStatementRepository(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient){
        return new StatementDynamoRepository(dynamoDbEnhancedAsyncClient);
    }

    @Bean
    public DynamoDbEnhancedAsyncClient getDynamoDbEnhancedAsyncClient(DynamoDbAsyncClient dynamoDbAsyncClient){
        return DynamoDbEnhancedAsyncClient.builder().dynamoDbClient(dynamoDbAsyncClient).build();
    }
}
