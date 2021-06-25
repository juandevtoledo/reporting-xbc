package com.lulobank.reporting.adapter.out.dynamodb.digitalevidence.mapper;

import com.lulobank.reporting.kernel.domain.entity.digitalevidence.document.Document;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientId;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DigitalEvidenceMapper {

    private DigitalEvidenceMapper() {
    }

    public static Map<String, AttributeValue> getClientIdKey(ClientId clientId) {
        return Map.of("idClient", AttributeValue.builder().s(clientId.getValue()).build());
    }

    public static Map<String, AttributeValue> getDocumentsValue(List<Document> documents) {
        return Map.of(":documents", getDocumentsListValue(documents));
    }

    public static AttributeValue getDocumentsListValue(List<Document> documents) {
        return AttributeValue.builder()
                .l(
                        documents.stream()
                                .map(DigitalEvidenceMapper::getDocAttributeValue)
                                .collect(Collectors.toList()))
                .build();

    }

    public static AttributeValue getDocAttributeValue(Document document) {
        return AttributeValue.builder()
                .m(Map.of(
                        "name", AttributeValue.builder().s(document.getDocumentName().getValue()).build(),
                        "documentInformation", AttributeValue.builder()
                                .m(
                                        Map.of("location",
                                                AttributeValue.builder().s(document.getDocumentInformation().getLocation().getValue()).build(),
                                                "acceptanceTimestamp",
                                                AttributeValue.builder().s(document.getDocumentInformation().getAcceptanceTimestamp().getValue().toString()).build())
                                ).build()

                )).build();
    }

}
