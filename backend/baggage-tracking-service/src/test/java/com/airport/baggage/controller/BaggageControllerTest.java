package com.airport.baggage.controller;

import com.airport.baggage.dto.BaggageResponse;
import com.airport.baggage.dto.RegisterBaggageRequest;
import com.airport.baggage.exception.GlobalExceptionHandler;
import com.airport.baggage.exception.NotFoundException;
import com.airport.baggage.model.BaggageStatus;
import com.airport.baggage.service.BaggageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BaggageController.class)
@Import(GlobalExceptionHandler.class)
public class BaggageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BaggageService baggageService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void registerBaggage_happyPath_returns201() throws Exception {
        RegisterBaggageRequest request = new RegisterBaggageRequest();
        request.setPassengerId(1L);
        request.setFlightId(10L);
        request.setWeightValue(new BigDecimal("23.5"));
        request.setWeightUnit("kg");

        BaggageResponse response = new BaggageResponse();
        response.setId(1L);
        response.setPassengerId(1L);
        response.setFlightId(10L);
        response.setTagNumber("TAG-ABC123");
        response.setBarcode("BAR-XYZ456");
        response.setStatus(BaggageStatus.REGISTERED);
        response.setCreatedAt(LocalDateTime.now());
        response.setUpdatedAt(LocalDateTime.now());

        when(baggageService.registerBaggage(any(RegisterBaggageRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/baggage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tagNumber").value("TAG-ABC123"))
                .andExpect(jsonPath("$.status").value("REGISTERED"));
    }

    @Test
    void getBaggage_notFound_returns404() throws Exception {
        when(baggageService.getBaggageById(99L))
                .thenThrow(new NotFoundException("Baggage not found with id: 99"));

        mockMvc.perform(get("/api/v1/baggage/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Baggage not found with id: 99"));
    }
}
