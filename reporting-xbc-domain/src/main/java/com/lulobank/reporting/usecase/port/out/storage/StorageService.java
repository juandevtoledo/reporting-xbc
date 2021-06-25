package com.lulobank.reporting.usecase.port.out.storage;

import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.FileFullPath;
import com.lulobank.reporting.kernel.domain.entity.types.FileType;
import com.lulobank.reporting.kernel.domain.entity.vo.ContentType;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public interface StorageService {

    Mono<Boolean> saveDocumentBucket(FileFullPath fileFullPath,
                                     ContentType contentType,
                                     ByteArrayOutputStream byteArrayOutputStream,
                                     Map<String, String> metadata,
                                     FileType fileType);

    Mono<String> getFileBase64(FileType type, FileFullPath fileFullPath);
}
