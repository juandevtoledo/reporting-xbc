package com.lulobank.reporting.adapter.out.s3;

import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.FileFullPath;
import com.lulobank.reporting.kernel.domain.entity.types.FileType;
import com.lulobank.reporting.kernel.domain.entity.vo.ContentType;
import com.lulobank.reporting.kernel.exception.StorageFileException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class StorageS3AdapterTest {

    @Mock
    private S3AsyncClient s3AsyncClient;

    private StorageS3Adapter target;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
        Map<String, String> bucketStorageName = Map.of(FileType.DIGITAL_EVIDENCE.name(), "digitalEvidenceBucket",
                FileType.POLICIES.name(), "policyBucket");

        target = new StorageS3Adapter(s3AsyncClient, bucketStorageName);
    }

    @Test(expected = StorageFileException.class)
    public void s3ReturnError() {
        when(s3AsyncClient.putObject(any(PutObjectRequest.class),any(AsyncRequestBody.class))).thenThrow(UnsupportedOperationException.class);

        Mono<Boolean> response = target.saveDocumentBucket(
                FileFullPath.builder().value("x").build(),
                ContentType.buildPDFContentType(),
                new ByteArrayOutputStream(),
                new HashMap<>(),
                FileType.STATEMENT
        );

        response.block();
    }

    @Test
    public void s3ReturnOk()  {
        when(s3AsyncClient.putObject(any(PutObjectRequest.class),any(AsyncRequestBody.class))).thenReturn(CompletableFuture.completedFuture(PutObjectResponse.builder().build()));

        Mono<Boolean> response = target.saveDocumentBucket(
                FileFullPath.builder().value("x").build(),
                ContentType.buildPDFContentType(),
                new ByteArrayOutputStream(),
                new HashMap<>(),
                FileType.STATEMENT
        );

        Assert.assertTrue(response.block());
    }


    @Test
    public void s3GetFileBase64ReturnOk()  {
        when(s3AsyncClient.getObject(any(GetObjectRequest.class),any(AsyncResponseTransformer.class)))
                .thenReturn(CompletableFuture.completedFuture(ResponseBytes.fromByteArray(AsyncResponseTransformer.toBytes(),
                        new byte[1])));

        Mono<String> response = target.getFileBase64(FileType.POLICIES,FileFullPath.builder().value("Path").build());

        StepVerifier
                .create(response)
                .expectNext("AA==")
                .verifyComplete();
    }

}
