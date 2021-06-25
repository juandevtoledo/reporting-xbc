package com.lulobank.reporting.adapter.out.pdf;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.lulobank.reporting.usecase.port.out.BuilderFileService;
import com.lulobank.reporting.usecase.port.out.SignerFileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.ITemplateEngine;

import java.util.Map;

@Configuration
public class ProcessPDFConfig {

    @Bean
    public BuilderFileService getBuilderPDFAdapter(ITemplateEngine templateEngine){
        return new BuilderPDFAdapter(templateEngine);
    }

    @Bean
    public SignerFileService getSignerFileService(BuilderFileService builderFileService){
        return new SignerPDFAdapter(builderFileService);
    }

    @Bean
    public HashOutputStreamAdapter getHashOutputStreamAdapter(){
        Map<String, HashFunction> hashFunctionMap = Map.of(
                "MD5", Hashing.md5(),
                "SHA-1", Hashing.sha1(),
                "SHA_256", Hashing.sha256());
        return new HashOutputStreamAdapter(hashFunctionMap);
    }

}
