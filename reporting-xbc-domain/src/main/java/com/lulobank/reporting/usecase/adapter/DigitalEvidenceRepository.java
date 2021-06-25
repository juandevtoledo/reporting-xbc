package com.lulobank.reporting.usecase.adapter;

import com.lulobank.reporting.kernel.domain.entity.digitalevidence.document.Document;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientId;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DigitalEvidenceRepository {

    Mono<Boolean> saveDigitalEvidenceDocuments(ClientId clientId, List<Document> documents);


}
