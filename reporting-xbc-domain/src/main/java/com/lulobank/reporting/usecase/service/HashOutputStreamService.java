package com.lulobank.reporting.usecase.service;

import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;

public interface HashOutputStreamService {

    Mono<ByteArrayOutputStream> generateTxtByContract(ByteArrayOutputStream outputStream);
}
