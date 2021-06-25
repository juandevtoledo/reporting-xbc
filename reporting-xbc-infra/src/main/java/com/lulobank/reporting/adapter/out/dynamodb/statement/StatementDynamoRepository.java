package com.lulobank.reporting.adapter.out.dynamodb.statement;

import com.lulobank.reporting.adapter.out.dynamodb.statement.dto.StatementEntity;
import com.lulobank.reporting.adapter.out.dynamodb.statement.mapper.StatementMapper;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientId;
import com.lulobank.reporting.kernel.domain.entity.statement.Statement;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ProductId;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ProductType;
import com.lulobank.reporting.kernel.exception.RepositoryException;
import com.lulobank.reporting.usecase.port.out.repository.StatementRepository;
import com.lulobank.reporting.usecase.port.out.repository.error.StatementRepositoryException;
import lombok.CustomLog;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.text.MessageFormat;
import java.util.Objects;
@CustomLog
public class StatementDynamoRepository implements StatementRepository {

    private final DynamoDbEnhancedAsyncClient enhancedClient;
    private static final String ERROR_MESSAGE = "Error trying to do operations with dynamo {0} {1}";

    public StatementDynamoRepository(DynamoDbEnhancedAsyncClient enhancedClient) {
        this.enhancedClient = enhancedClient;
    }

    @Override
    public Mono<Statement> findById(ClientId clientId, ProductType productType, ProductId productId) {
        return Mono.just(enhancedClient.table("Reporting", TableSchema.fromBean(StatementEntity.class)))
                .flatMap(mappedTable -> Mono.fromFuture(mappedTable.getItem(r -> r.key(buildKey(clientId, productType, productId))))
                        .publishOn(Schedulers.elastic())
                        .filter(Objects::nonNull)
                        .map(StatementMapper::toStatement)
                        .doOnError(e -> log.error(MessageFormat.format(ERROR_MESSAGE, e, e)))
                        .onErrorResume(e -> Mono.error(StatementRepositoryException.getDefaultError()))

                );

    }

    @Override
    public Mono<Statement> save(Statement statement) {
        return Mono.just(enhancedClient.table("Reporting", TableSchema.fromBean(StatementEntity.class)))
                .flatMap(mappedTable -> Mono.fromFuture(mappedTable.putItem(StatementMapper.toStatementEntity(statement)))
                        .publishOn(Schedulers.elastic())
                        .thenReturn(statement)
                        .doOnError(e -> log.error(MessageFormat.format(ERROR_MESSAGE, e, e)))
                        .onErrorResume(e -> Mono.error(new RepositoryException(MessageFormat.format(ERROR_MESSAGE, e.getMessage()))))

                );
    }

    private Key buildKey(ClientId clientId, ProductType productType, ProductId productId) {
        String SHORT_KEY_SEPARATOR = "#";
        return Key.builder()
                .partitionValue(clientId.getValue())
                .sortValue(productType.getValue().concat(SHORT_KEY_SEPARATOR).concat(productId.getValue()))
                .build();
    }





}
