package com.airport.retail.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpCheckInValidationClient implements CheckInValidationClient {

    private final RestTemplate restTemplate;
    private final String checkInServiceUrl;
    private final boolean validationEnabled;

    public HttpCheckInValidationClient(RestTemplateBuilder restTemplateBuilder,
                                       @Value("${app.services.passenger-checkin-url}") String checkInServiceUrl,
                                       @Value("${app.checkin-validation.enabled}") boolean validationEnabled) {
        this.restTemplate = restTemplateBuilder.build();
        this.checkInServiceUrl = checkInServiceUrl;
        this.validationEnabled = validationEnabled;
    }

    @Override
    public Long validatePassengerCheckedIn(Long passengerId, Long flightId) {
        if (!validationEnabled) {
            return 0L;
        }
        try {
            CheckInResponse response = restTemplate.getForObject(
                    checkInServiceUrl + "/api/v1/checkins/passenger/" + passengerId + "/flight/" + flightId,
                    CheckInResponse.class
            );
            if (response == null || response.id() == null) {
                throw new IllegalStateException("Passenger check-in could not be validated");
            }
            return response.id();
        } catch (RestClientException ex) {
            throw new IllegalStateException("Passenger must be checked in before duty-free ordering");
        }
    }

    private record CheckInResponse(Long id) {
    }
}
