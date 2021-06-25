package com.lulobank.reporting.usecase.port.out;

import com.lulobank.reporting.kernel.domain.entity.person.ClientInformation;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientIdCBS;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.File;

public interface SignerFileService {

    Mono<ByteArrayOutputStream> singFile(final ByteArrayOutputStream pdfOutputStream, final ClientIdCBS clientIdCBS);

    Mono<File> addSignaturePage(File value, ClientInformation clientInformation);

}
