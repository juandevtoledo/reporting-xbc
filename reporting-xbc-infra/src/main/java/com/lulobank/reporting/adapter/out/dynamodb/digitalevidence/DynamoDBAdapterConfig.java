package com.lulobank.reporting.adapter.out.dynamodb.digitalevidence;

import com.lulobank.reporting.usecase.adapter.DigitalEvidenceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

@Configuration
public class DynamoDBAdapterConfig {

    @Bean
    public DigitalEvidenceRepository getRepositoryService(DynamoDbAsyncClient dynamoDbAsyncClient){
        return new DynamoDBAdapter(dynamoDbAsyncClient);
    }

}
