package com.lulobank.reporting.adapter.out.dynamodb.statement;

import com.lulobank.reporting.adapter.out.dynamodb.statement.dynamoenhacedimp.DynamoEmptyImpl;
import com.lulobank.reporting.adapter.out.dynamodb.statement.dynamoenhacedimp.DynamoFailImpl;
import com.lulobank.reporting.adapter.out.dynamodb.statement.dynamoenhacedimp.DynamoSuccessImpl;
import com.lulobank.reporting.adapter.out.dynamodb.statement.mapper.StatementMapper;
import com.lulobank.reporting.adapter.out.dynamodb.statement.util.SampleDynamo;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientId;
import com.lulobank.reporting.kernel.domain.entity.statement.Statement;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ProductId;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ProductType;
import com.lulobank.reporting.kernel.exception.RepositoryException;
import com.lulobank.reporting.usecase.port.out.repository.error.StatementRepositoryException;
import com.lulobank.reporting.utils.Sample;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.mapper.BeanTableSchema;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class StatementDynamoRepositoryTest {

    @Mock
    private DynamoDbEnhancedAsyncClient enhancedClient;

    private StatementDynamoRepository statementDynamoRepository;

    private ClientId clientId;
    private ProductType productType;
    private ProductId productId;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        clientId = ClientId.builder().value("c12e7fc3-f730-4737-b476-73c41723a0ed\"").build();
        productId = ProductId.builder().value("23246250630").build();
        productType = ProductType.buildProductLoan();
        statementDynamoRepository = new StatementDynamoRepository(enhancedClient);
    }

    @Test
    public void findByIdFailTest() {
        when(enhancedClient.table(any(), any(BeanTableSchema.class))).thenReturn(new DynamoFailImpl());

        Mono<Statement> response = statementDynamoRepository.findById(clientId, productType, productId);
        StepVerifier.create(response).expectError(StatementRepositoryException.class).verifyThenAssertThat();

    }

    @Test
    public void findByIdEmptyResult() {
        when(enhancedClient.table(any(), any(BeanTableSchema.class))).thenReturn(new DynamoEmptyImpl());

        Mono<Statement> response = statementDynamoRepository.findById(clientId, productType, productId);
        StepVerifier.create(response).expectNext().expectComplete().verifyThenAssertThat();

    }

    @Test
    public void findByIdSuccessResult() {
        when(enhancedClient.table(any(), any(BeanTableSchema.class))).thenReturn(new DynamoSuccessImpl());

        Mono<Statement> response = statementDynamoRepository.findById(clientId, productType, productId);

        StepVerifier.create(response)
                .expectNextMatches(s -> s.getClientId().getValue().equals(StatementMapper.toStatement(SampleDynamo.buildStatementEntity()).getClientId().getValue())
                        && s.getListFiles().size() == StatementMapper.toStatement(SampleDynamo.buildStatementEntity()).getListFiles().size()
                        && s.getProductId().getValue().equals(StatementMapper.toStatement(SampleDynamo.buildStatementEntity()).getProductId().getValue())
                        && s.getProductType().getValue().equals(StatementMapper.toStatement(SampleDynamo.buildStatementEntity()).getProductType().getValue())

                )
                .expectComplete().verifyThenAssertThat();
    }

    @Test
    public void saveFail(){
        when(enhancedClient.table(any(), any(BeanTableSchema.class))).thenReturn(new DynamoFailImpl());

        Mono<Statement> response = statementDynamoRepository.save(Sample.getStatement());

        StepVerifier.create(response).expectError(RepositoryException.class).verifyThenAssertThat();

    }

    @Test
    public void saveSuccess(){
        when(enhancedClient.table(any(), any(BeanTableSchema.class))).thenReturn(new DynamoSuccessImpl());

        Mono<Statement> response = statementDynamoRepository.save(Sample.getStatement());

        StepVerifier.create(response)
                .expectNextMatches(s -> s.getClientId().getValue().equals(Sample.getStatement().getClientId().getValue())
                )
                .expectComplete().verifyThenAssertThat();

    }
}
