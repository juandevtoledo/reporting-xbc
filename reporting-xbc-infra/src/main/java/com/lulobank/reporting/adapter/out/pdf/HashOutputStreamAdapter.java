package com.lulobank.reporting.adapter.out.pdf;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.io.ByteSource;
import com.lulobank.reporting.kernel.exception.BuildHashFileException;
import com.lulobank.reporting.usecase.service.HashOutputStreamService;
import io.vavr.control.Try;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

public class HashOutputStreamAdapter implements HashOutputStreamService {

    private final Map<String, HashFunction> hashFunctionMap;

    public HashOutputStreamAdapter(Map<String, HashFunction> hashFunctionMap){
        this.hashFunctionMap = hashFunctionMap;
    }

    @Override
    public Mono<ByteArrayOutputStream> generateTxtByContract(ByteArrayOutputStream byteArrayOutputStream) {
        return Flux.fromStream(hashFunctionMap.entrySet().stream())
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .flatMap(entrySet -> hashOutputStrean(byteArrayOutputStream, entrySet))
                .sequential()
                .collectList()
                .flatMap(this::createOutputStream)
                .onErrorResume(error -> Mono.error(new BuildHashFileException(error.getMessage())));
    }

    private Mono<ByteArrayOutputStream> createOutputStream(List<String> hashes) {
        return Mono.using(ByteArrayOutputStream::new, baos -> Flux.fromIterable(hashes)
                .reduce((accumulate, current)->accumulate.concat("\n").concat(current))
                .flatMap(content-> Mono.fromCallable(()-> {
                    baos.write(content.getBytes());
                    return baos;
                })), baos -> Try.run(baos::close));
    }

    private Mono<String> hashOutputStrean(ByteArrayOutputStream byteArrayOutputStream,
                                          Map.Entry<String, HashFunction> hashing) {
        return Mono.fromCallable(() -> ByteSource.wrap(byteArrayOutputStream.toByteArray()).hash(hashing.getValue()))
                .map(HashCode::toString)
                .map(hash -> hashing.getKey().concat(":").concat(hash));
    }
}
