package com.lulobank.reporting.usecase.port.out.queue;

import com.lulobank.reporting.kernel.domain.entity.FatcaCrsInformation;
import reactor.core.publisher.Mono;

public interface ClientNotificationService {

    Mono<FatcaCrsInformation> digitalEvidenceSuccessful(FatcaCrsInformation fatcaCrsInformation);

    Mono<FatcaCrsInformation> digitalEvidenceFailed(FatcaCrsInformation fatcaCrsInformation);
}
