package com.airport.gate.config;

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
                        .title("Gate Management Service API")
                        .description("Assigns and manages airport gates. Tracks availability and assigns based on flight type and aircraft size.")
                        .version("v1")
                        .contact(new Contact().name("Ibrahim Alibayov"))
                        .license(new License().name("Apache 2.0")));
    }
}
