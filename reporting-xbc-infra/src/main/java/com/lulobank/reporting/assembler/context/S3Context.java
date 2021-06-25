package com.lulobank.reporting.assembler.context;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Configuration
public class S3Context {

    @Value("${cloud.aws.region.static}")
    private String s3Region;

    @Bean
    public S3AsyncClient s3Client() {
        return  S3AsyncClient.builder()
                .region(Region.of(s3Region))
                .build();
    }
}
