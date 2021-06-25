package com.lulobank.reporting.adapter.out.s3.config;

import com.lulobank.reporting.adapter.out.s3.StorageS3Adapter;
import com.lulobank.reporting.kernel.domain.entity.types.FileType;
import com.lulobank.reporting.usecase.port.out.storage.StorageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import java.util.Map;

@Configuration
public class StorageS3AdapterConfig {

    @Value("${cloud.aws.s3.statement.name}")
    private String bucketNameStatement;
    @Value("${cloud.aws.s3.statement-transient.name}")
    private String bucketNameTransitStatement;
    @Value("${cloud.aws.s3.digitalevidence.bucket.policies}")
    private String bucketPolicies;
    @Value("${cloud.aws.s3.digitalevidence.bucket.evidence}")
    private String bucketDigitalEvidence;

    @Bean
    public StorageService storageService(S3AsyncClient s3AsyncClient,
                                         @Qualifier("buckets") Map<String, String> bucketToStorageName) {
        return new StorageS3Adapter(s3AsyncClient, bucketToStorageName);
    }

    @Bean(name = "buckets")
    public Map<String, String> getBucketName() {
        return Map.of(
                FileType.DIGITAL_EVIDENCE.name(), bucketDigitalEvidence,
                FileType.STATEMENT_TRANSIT.name(), bucketNameTransitStatement,
                FileType.STATEMENT.name(), bucketNameStatement,
                FileType.POLICIES.name(), bucketPolicies
        );
    }
}
