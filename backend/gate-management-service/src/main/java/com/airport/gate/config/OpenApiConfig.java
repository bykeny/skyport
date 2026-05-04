package com.airport.gate.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI gateOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gate Management API")
                        .description("Endpoints for gate catalog, statuses, and flight gate-assignments.")
                        .version("v1")
                        .contact(new Contact().name("Airport Management System"))
                        .license(new License().name("Apache 2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("Project docs")
                        .url("https://example.com/docs"));
    }
}
