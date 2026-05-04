package com.airport.checkin.controller;

import com.airport.checkin.dto.CheckInResponse;
import com.airport.checkin.dto.CreateCheckInRequest;
import com.airport.checkin.exception.GlobalExceptionHandler;
import com.airport.checkin.exception.NotFoundException;
import com.airport.checkin.model.CheckInStatus;
import com.airport.checkin.service.CheckInService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CheckInController.class)
@Import({GlobalExceptionHandler.class, CheckInControllerTest.TestCheckInServiceConfig.class})
class CheckInControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void createCheckIn_happyPath_returns201() throws Exception {
        CreateCheckInRequest request = new CreateCheckInRequest();
        request.setPassengerId(1L);
        request.setFlightId(10L);
        request.setSeatNumber("12A");
        request.setBaggageCount(1);

        mockMvc.perform(post("/api/v1/checkins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.boardingPassCode").value("BP-ABC1234567"))
                .andExpect(jsonPath("$.status").value("CHECKED_IN"));
    }

    @Test
    void getCheckIn_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/v1/checkins/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Check-in not found with id: 99"));
    }

    @TestConfiguration
    static class TestCheckInServiceConfig {

        @Bean
        CheckInService checkInService() {
            return new CheckInService(null, null, null) {
                @Override
                public CheckInResponse createCheckIn(CreateCheckInRequest request) {
                    CheckInResponse response = new CheckInResponse();
                    response.setId(100L);
                    response.setPassengerId(request.getPassengerId());
                    response.setFlightId(request.getFlightId());
                    response.setSeatNumber(request.getSeatNumber());
                    response.setBoardingPassCode("BP-ABC1234567");
                    response.setBaggageCount(request.getBaggageCount());
                    response.setStatus(CheckInStatus.CHECKED_IN);
                    response.setCheckedInAt(LocalDateTime.now());
                    return response;
                }

                @Override
                public CheckInResponse getCheckIn(Long id) {
                    throw new NotFoundException("Check-in not found with id: " + id);
                }
            };
        }
    }
}
