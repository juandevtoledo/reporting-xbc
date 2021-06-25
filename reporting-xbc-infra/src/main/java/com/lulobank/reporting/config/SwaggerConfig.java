package com.lulobank.reporting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
    private static final String HTTPURL = "https://github.com/piso19";
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }
    private ApiInfo getApiInfo() {
        final Contact contactInfo = new Contact("Lulo Bank", HTTPURL, "tbd@lulobank.com");
        return new ApiInfo(
                "API Rest de servicio de Reporting-xbc",
                "Aqui vive el API de  Reporting-xbc",
                "0.0.0", HTTPURL,
                contactInfo,
                "Copyright", HTTPURL, Collections.EMPTY_LIST);

    }

}
