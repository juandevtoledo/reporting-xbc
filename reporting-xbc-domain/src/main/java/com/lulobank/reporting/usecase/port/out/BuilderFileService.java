package com.lulobank.reporting.usecase.port.out;

import com.lulobank.reporting.kernel.domain.entity.digitalevidence.DigitalEvidencePageInformation;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public interface BuilderFileService {

    Mono<ByteArrayOutputStream> buildFile(final String templateName, final Map<String, Object> map);

    Mono<ByteArrayOutputStream> addDigitalEvidencePage(ByteArrayOutputStream byteArrayOutputStream, DigitalEvidencePageInformation digitalEvidencePageInformation);
}
