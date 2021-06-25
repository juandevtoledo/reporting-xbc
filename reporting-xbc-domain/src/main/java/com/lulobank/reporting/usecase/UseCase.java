package com.lulobank.reporting.usecase;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface UseCase<Command, R> {
    Mono<R> execute(Command command);
}