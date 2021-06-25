package com.lulobank.reporting.adapter.out.dynamodb.digitalevidence;

import com.lulobank.reporting.adapter.out.dynamodb.digitalevidence.mapper.DigitalEvidenceMapper;
import com.lulobank.reporting.kernel.domain.entity.digitalevidence.document.Document;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientId;
import com.lulobank.reporting.kernel.exception.RepositoryException;
import com.lulobank.reporting.usecase.adapter.DigitalEvidenceRepository;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@CustomLog
public class DynamoDBAdapter implements DigitalEvidenceRepository {

    private static final String DIGITAL_EVIDENCE_TABLE = "DigitalEvidence";

    private final DynamoDbAsyncClient dynamoClient;


    @Override
    public Mono<Boolean> saveDigitalEvidenceDocuments(ClientId clientId, List<Document> documents) {
        return Mono.just(UpdateItemRequest.builder()
                .tableName(DIGITAL_EVIDENCE_TABLE)
                .key(DigitalEvidenceMapper.getClientIdKey(clientId))
                .updateExpression("set documents = list_append(documents, :documents )")
                .expressionAttributeValues(DigitalEvidenceMapper.getDocumentsValue(documents))
                .build())
        .flatMap(this::runUpdateItem)
                .filter(Objects::nonNull)
                .map(putObjectResponse -> true)
                .doOnError(e -> log.error("Error updating digital evidence." + e.getMessage()))
                .onErrorMap(e -> new RepositoryException(MessageFormat.format("Error updating digital evidence. {0}", e)))
                .switchIfEmpty(Mono.error(new RepositoryException("Error updating digital evidence.")));
    }

    private Mono<UpdateItemResponse> runUpdateItem(UpdateItemRequest updateItemRequest){
        return Mono.fromFuture(() -> dynamoClient.updateItem(updateItemRequest))
                .publishOn(Schedulers.elastic());
    }


}
