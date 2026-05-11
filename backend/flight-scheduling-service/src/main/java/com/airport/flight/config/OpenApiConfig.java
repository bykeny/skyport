package com.airport.flight.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI flightOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Flight Scheduling Service API")
                        .description("Manages flight schedules, status updates, delays, and cancellations. Central data hub for all other services.")
                        .version("v1")
                        .contact(new Contact().name("Ibrahim Alibayov"))
                        .license(new License().name("Apache 2.0")));
    }
}
