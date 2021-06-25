package com.lulobank.reporting.config.wrapper;

import com.lulobank.core.security.spring.LuloBankJwtDecoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import reactor.core.publisher.Mono;

public class ReactiveJwtDecodeWrapper implements ReactiveJwtDecoder {

    private LuloBankJwtDecoder jwtDecoder;

    public ReactiveJwtDecodeWrapper(LuloBankJwtDecoder jwtDecoder){
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public Mono<Jwt> decode(String token) {
        return Mono.just(jwtDecoder.decode(token));
    }
}
