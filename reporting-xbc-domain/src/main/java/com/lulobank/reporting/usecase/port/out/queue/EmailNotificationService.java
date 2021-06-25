package com.lulobank.reporting.usecase.port.out.queue;

import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.FileFullPath;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientId;
import com.lulobank.reporting.kernel.domain.entity.person.vo.Email;
import com.lulobank.reporting.kernel.domain.entity.vo.ContentType;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface EmailNotificationService {

    Mono<Void> emailNotificationFatcaDocuments(ClientId clientId,
                                               Email email,
                                               List<FileFullPath> fileFullPath,
                                               ContentType contentType,
                                               Map<String, Object> dates);

}
