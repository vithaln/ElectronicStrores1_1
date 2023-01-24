package com.vithal.electronic.store.config;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
@Configuration
public class SwaggerConfig {
	@Bean
    public Docket docket() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(getApiInfo());
        docket.securityContexts(Arrays.asList(getSecurityContext()));
        docket.securitySchemes(Arrays.asList(getSchemes()));
        ApiSelectorBuilder select = docket.select();
        select.apis(RequestHandlerSelectors.any());
        select.paths(PathSelectors.any());
        Docket build = select.build();
        return build;
    }


    private SecurityContext getSecurityContext() {

        SecurityContext context = SecurityContext
                .builder()
                .securityReferences(getSecurityReferences())
                .build();
        return context;
    }

    private List<SecurityReference> getSecurityReferences() {
        AuthorizationScope[] scopes = {new AuthorizationScope("Global", "Access Every Thing")};
        return Arrays.asList(new SecurityReference("JWT", scopes));

    }

    private ApiKey getSchemes() {
        return new ApiKey("JWT", "Authorization", "header");
    }


    private ApiInfo getApiInfo() {

        ApiInfo apiInfo = new ApiInfo(
                "Electronic Store Backend : APIS ",
                "This is backend project created by Vithal N",
                "1.0.0V",
                "",
                new Contact("Vithal", "", "vnnivargi1998@gmail.com"),
                "License of APIS",
                "",
                new ArrayDeque<>()
        );

        return apiInfo;

    }

}
