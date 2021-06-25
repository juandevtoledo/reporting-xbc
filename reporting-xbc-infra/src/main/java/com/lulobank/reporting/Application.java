package com.lulobank.reporting;

import brave.sampler.Sampler;
import com.lulobank.events.reactive.config.EnableEventsReactive;
import com.lulobank.http.reactive.EnableHttpReactive;
import com.lulobank.reporting.adapter.in.ReportUseCaseConfig;
import com.lulobank.reporting.adapter.in.UseCaseConfig;
import com.lulobank.reporting.adapter.in.sqs.SqsRegistryHandlerConfig;
import com.lulobank.reporting.adapter.out.dynamodb.digitalevidence.DynamoDBAdapterConfig;
import com.lulobank.reporting.adapter.out.dynamodb.statement.StatementDynamoRepositoryConfig;
import com.lulobank.reporting.adapter.out.flexibility.LoanFlexibilityAdapterConfig;
import com.lulobank.reporting.adapter.out.pdf.ProcessPDFConfig;
import com.lulobank.reporting.adapter.out.s3.config.StorageS3AdapterConfig;
import com.lulobank.reporting.assembler.context.DynamoDBContext;
import com.lulobank.reporting.assembler.context.S3Context;
import com.lulobank.reporting.handler.config.HandlerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.Locale;
import java.util.TimeZone;

@SpringBootApplication()
@ComponentScan(
        basePackages = {
                "com.lulobank.reporting.config"
        },
        basePackageClasses = {
                S3Context.class,
                LoanFlexibilityAdapterConfig.class,
                SqsRegistryHandlerConfig.class,
                StorageS3AdapterConfig.class,
                UseCaseConfig.class,
                ProcessPDFConfig.class,
                DynamoDBContext.class,
                DynamoDBAdapterConfig.class,
                StatementDynamoRepositoryConfig.class,
                HandlerConfig.class,
                ReportUseCaseConfig.class,
                EnableEventsReactive.class,
                EnableHttpReactive.class
        })
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
        TimeZone.setDefault(TimeZone.getTimeZone("America/Bogota"));
        Locale.setDefault(Locale.forLanguageTag("es-CO"));
    }

    @Bean
    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }

}