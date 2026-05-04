package com.airport.checkin.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpFlightValidationClient implements FlightValidationClient {

    private final RestTemplate restTemplate;
    private final String flightServiceUrl;
    private final boolean validationEnabled;

    public HttpFlightValidationClient(RestTemplateBuilder restTemplateBuilder,
                                      @Value("${app.services.flight-scheduling-url}") String flightServiceUrl,
                                      @Value("${app.flight-validation.enabled}") boolean validationEnabled) {
        this.restTemplate = restTemplateBuilder.build();
        this.flightServiceUrl = flightServiceUrl;
        this.validationEnabled = validationEnabled;
    }

    @Override
    public void validateFlightExists(Long flightId) {
        if (!validationEnabled) {
            return;
        }
        try {
            restTemplate.getForEntity(flightServiceUrl + "/api/v1/flights/" + flightId, String.class);
        } catch (RestClientException ex) {
            throw new IllegalStateException("Flight could not be validated: " + flightId);
        }
    }
}
