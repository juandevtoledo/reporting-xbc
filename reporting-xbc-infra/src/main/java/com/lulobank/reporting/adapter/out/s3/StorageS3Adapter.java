package com.lulobank.reporting.adapter.out.s3;

import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.FileFullPath;
import com.lulobank.reporting.kernel.domain.entity.types.FileType;
import com.lulobank.reporting.kernel.domain.entity.vo.ContentType;
import com.lulobank.reporting.kernel.exception.StorageFileException;
import com.lulobank.reporting.usecase.port.out.storage.StorageService;
import com.lulobank.reporting.usecase.port.out.storage.error.StatementStorageException;
import lombok.CustomLog;
import org.apache.commons.codec.binary.Base64;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.core.BytesWrapper;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Objects;

@CustomLog
public class StorageS3Adapter implements StorageService {

    private final S3AsyncClient s3Client;
    private final Map<String, String> bucketToStorageName;
    private static final String ERROR_MESSAGE = "Error trying to upload PDF {0}";


    public StorageS3Adapter(S3AsyncClient s3Client,
                            Map<String, String> bucketToStorageName) {
        this.s3Client = s3Client;
        this.bucketToStorageName = bucketToStorageName;
    }

    @Override
    public Mono<Boolean> saveDocumentBucket(FileFullPath fileFullPath, ContentType contentType, ByteArrayOutputStream byteArrayOutputStream,
                                            Map<String, String> metadata, FileType fileType) {

        return Mono.just(PutObjectRequest.builder()
                .contentType(contentType.getValue())
                .metadata(metadata)
                .bucket(getBucketName(fileType))
                .key(fileFullPath.getValue())
                .build())
                .flatMap(putObjectRequest -> saveFileBucket(putObjectRequest, byteArrayOutputStream));
    }


    @Override
    public Mono<String> getFileBase64(FileType type, FileFullPath fileFullPath) {
        return getFileByBucket(bucketToStorageName.get(type.name()),fileFullPath.getValue())
                .map(BytesWrapper::asByteArray)
                .map(Base64::encodeBase64)
                .map(encoded -> new String(encoded, StandardCharsets.US_ASCII))
                .onErrorMap(e -> {
                    log.error(MessageFormat.format("Error getting file from bucket. {0}", e));
                    return StatementStorageException.defaultStorageServiceError();
                });
    }

    private Mono<ResponseBytes<GetObjectResponse>> getFileByBucket(String bucket, String key) {

        return Mono.just(GetObjectRequest.builder().bucket(bucket).key(key).build())
                .flatMap(request -> Mono.fromFuture(() -> s3Client.getObject(request, AsyncResponseTransformer.toBytes()))
                        .publishOn(Schedulers.elastic()));

    }

    private Mono<Boolean> saveFileBucket(PutObjectRequest putObjectRequest, ByteArrayOutputStream byteArrayOutputStream) {
        return runPutObject(byteArrayOutputStream, putObjectRequest)
                .filter(Objects::nonNull)
                .map(putObjectResponse -> true)
                .onErrorResume(e -> Mono.error(new StorageFileException(MessageFormat.format(ERROR_MESSAGE, e))))
                .switchIfEmpty(Mono.error(new StorageFileException(ERROR_MESSAGE)))
                .doOnError(e -> log.error(e.getMessage()));
    }

    private Mono<PutObjectResponse> runPutObject(ByteArrayOutputStream byteArrayOutputStream, PutObjectRequest putObjectRequest) {
        return Mono.fromFuture(() -> s3Client.putObject(putObjectRequest,
                AsyncRequestBody.fromByteBuffer(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()))))
                .publishOn(Schedulers.elastic());
    }

    private String getBucketName(FileType fileType) {
        return bucketToStorageName.get(fileType.name());
    }

}
