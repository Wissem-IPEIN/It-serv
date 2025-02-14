package com.wkrzywiec.medium.kanban.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final Logger logger = LoggerFactory.getLogger(SwaggerConfig.class);

    @Bean
    public Docket api() {
        logger.info("Initializing Swagger configuration...");
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        logger.info("Setting up API information for Swagger UI...");
        return new ApiInfo(
                "Kanban REST API",
                "This is a REST API of Kanban REST API, where you can get/add/remove/modify Kanban board and its task.",
                "v1",
                "Terms of service",
                new Contact("Wojtek Krzywiec", "www.github.com/wkrzywiec", ""),
                "License of API", "API license URL", Collections.emptyList()
        );
    }
}
