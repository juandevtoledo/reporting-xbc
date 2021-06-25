package com.lulobank.reporting.adapter.out.dynamodb;

import com.lulobank.reporting.adapter.out.dynamodb.digitalevidence.DynamoDBAdapter;
import com.lulobank.reporting.kernel.domain.entity.digitalevidence.document.Document;
import com.lulobank.reporting.kernel.exception.RepositoryException;
import com.lulobank.reporting.utils.Sample;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.lulobank.reporting.utils.Sample.ID_CLIENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class DynamoDBAdapterTest {

    private static final String DIGITAL_EVIDENCE_TABLE = "DigitalEvidence";

    @Mock
    private DynamoDbAsyncClient dynamoDbAsyncClient;

    private DynamoDBAdapter dynamoDBAdapter;

    @Captor
    private ArgumentCaptor<UpdateItemRequest> updateItemRequestArgumentCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dynamoDBAdapter = new DynamoDBAdapter(dynamoDbAsyncClient);
    }

    @Test
    public void shouldReturnRepositoryException() {
        List<Document> documents =  Sample.getListDocuments();
        when(dynamoDbAsyncClient.updateItem(updateItemRequestArgumentCaptor.capture()))
                .thenThrow(UnsupportedOperationException.class);
        Mono<Boolean> save = dynamoDBAdapter.saveDigitalEvidenceDocuments(Sample.getClientId(), documents);
        StepVerifier
                .create(save)
                .expectError(RepositoryException.class)
                .verify();
        assertEquals(DIGITAL_EVIDENCE_TABLE, updateItemRequestArgumentCaptor.getValue().tableName());
        assertEquals(ID_CLIENT, updateItemRequestArgumentCaptor.getValue().key().get("idClient").s());
        assertEquals(Sample.getDocumentsValue(documents),updateItemRequestArgumentCaptor.getValue().expressionAttributeValues());

    }

    @Test
    public void shouldReturnOkUpdate() {
        List<Document> documents =  Sample.getListDocuments();
        when(dynamoDbAsyncClient.updateItem(updateItemRequestArgumentCaptor.capture()))
                .thenReturn(CompletableFuture.completedFuture(UpdateItemResponse.builder().build()));
        Mono<Boolean> save = dynamoDBAdapter.saveDigitalEvidenceDocuments(Sample.getClientId(), documents);
        StepVerifier
                .create(save)
                .expectNext(true)
                .verifyComplete();
        assertEquals(DIGITAL_EVIDENCE_TABLE, updateItemRequestArgumentCaptor.getValue().tableName());
        assertEquals(ID_CLIENT, updateItemRequestArgumentCaptor.getValue().key().get("idClient").s());
        assertEquals(Sample.getDocumentsValue(documents),updateItemRequestArgumentCaptor.getValue().expressionAttributeValues());

    }

}
