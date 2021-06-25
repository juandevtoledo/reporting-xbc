package com.lulobank.reporting.assembler.context;

import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class DynamoDBContext {

    @Value("${cloud.aws.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Bean
    public DynamoDbAsyncClient amazonDynamoDB() {
        return DynamoDbAsyncClient.builder()
                .endpointOverride(URI.create(amazonDynamoDBEndpoint))
                .build();
    }
}