package com.lulobank.reporting.usecase.util;

import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientId;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ProductId;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ProductType;
import io.vavr.Tuple;
import io.vavr.Tuple3;
import reactor.core.publisher.Mono;

public class Util {

    public static Mono<Tuple3<ClientId, ProductType, ProductId>> buildRequestForStatementRepository(String clientId,
                                                                                                    String productType,
                                                                                                    String productId) {
        return Mono.just(Tuple.of(
                ClientId.builder().value(clientId).build(),
                ProductType.builder().value(productType).build(),
                ProductId.builder().value(productId).build()
        ));
    }
}
