package com.blogapp.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;

@Configuration
public class SwaggerConfig {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    public ApiKey apiKey(){
        return new ApiKey("JWT",AUTHORIZATION_HEADER,"header");
    }

    private List<SecurityContext> securityContexts(){
        return Collections.singletonList(SecurityContext.builder().securityReferences(securityReferences()).build());
    }

    private List<SecurityReference> securityReferences(){
        AuthorizationScope scope = new AuthorizationScope("global","access everything");
        return List.of(new SecurityReference("JWT", new AuthorizationScope[]{scope}));
    }

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getInfo())
                .securityContexts(securityContexts())
                .securitySchemes(Collections.singletonList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();

    }

    private ApiInfo getInfo() {
        return new ApiInfo("Blogging Application : Backend Info","This project is developed using Spring Boot V-2.7.3 .","0.0.5","Terms of service",new Contact("Soumik Sarkar (Associate Software Developer)","null","SaikatSarkar502@gmail.com"),"License ","null", Collections.emptyList());
    }
}
