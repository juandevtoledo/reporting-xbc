package com.lulobank.reporting.adapter.in;

import com.lulobank.reporting.adapter.in.util.ReportType;
import com.lulobank.reporting.adapter.out.sqs.clients.config.MessageClientsConfig;
import com.lulobank.reporting.adapter.out.sqs.email.config.EmailNotificationServiceAdapterConfig;
import com.lulobank.reporting.usecase.adapter.DigitalEvidenceRepository;
import com.lulobank.reporting.usecase.port.out.BuilderFileService;
import com.lulobank.reporting.usecase.port.out.SignerFileService;
import com.lulobank.reporting.usecase.port.out.queue.ClientNotificationService;
import com.lulobank.reporting.usecase.port.out.queue.EmailNotificationService;
import com.lulobank.reporting.usecase.port.out.storage.StorageService;
import com.lulobank.reporting.usecase.report.fatca.CreateFatcaReportUseCase;
import com.lulobank.reporting.usecase.service.HashOutputStreamService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({MessageClientsConfig.class, EmailNotificationServiceAdapterConfig.class})
public class ReportUseCaseConfig {

    @ReportType(name = "FatcaCrsDataReport")
    public CreateFatcaReportUseCase getCreateFatcaReportUseCase(BuilderFileService builderFileService,
                                                                SignerFileService signerFileService,
                                                                StorageService storageService,
                                                                HashOutputStreamService hashOutputStreamService,
                                                                DigitalEvidenceRepository digitalEvidenceRepository,
                                                                ClientNotificationService clientNotificationService,
                                                                EmailNotificationService emailNotificationService) {
        return new CreateFatcaReportUseCase(builderFileService, signerFileService, storageService,
                hashOutputStreamService, digitalEvidenceRepository, clientNotificationService, emailNotificationService);
    }
}
