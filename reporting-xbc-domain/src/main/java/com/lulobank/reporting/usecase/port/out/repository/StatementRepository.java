package com.lulobank.reporting.usecase.port.out.repository;

import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientId;
import com.lulobank.reporting.kernel.domain.entity.statement.Statement;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ProductId;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ProductType;
import reactor.core.publisher.Mono;

public interface StatementRepository {

    Mono<Statement> findById(ClientId clientId, ProductType productType, ProductId productId);

    Mono<Statement> save(Statement statement);

}