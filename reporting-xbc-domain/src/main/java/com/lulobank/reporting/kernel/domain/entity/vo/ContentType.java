package com.lulobank.reporting.kernel.domain.entity.vo;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class ContentType extends PrimitiveVo<String>{
    private static final String CONTENT_TYPE = "application/pdf";
    private static final String CONTENT_TYPE_TXT = "text/plain";

    public static ContentType buildPDFContentType(){
        return ContentType.builder().value(CONTENT_TYPE).build();
    }

    public static ContentType buildTXTContentType(){
        return ContentType.builder().value(CONTENT_TYPE_TXT).build();
    }
}
