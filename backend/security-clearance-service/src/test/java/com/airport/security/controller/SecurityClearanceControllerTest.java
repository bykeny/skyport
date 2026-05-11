package com.airport.security.controller;

import com.airport.security.dto.ClearanceResponse;
import com.airport.security.dto.CreateClearanceRequest;
import com.airport.security.exception.GlobalExceptionHandler;
import com.airport.security.exception.NotFoundException;
import com.airport.security.model.ClearanceStatus;
import com.airport.security.service.SecurityClearanceService;
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

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SecurityClearanceController.class)
@Import(GlobalExceptionHandler.class)
public class SecurityClearanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SecurityClearanceService service;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void createClearance_happyPath_returns201() throws Exception {
        CreateClearanceRequest request = new CreateClearanceRequest();
        request.setPassengerId(1L);
        request.setCheckInId(5L);
        request.setScreeningZone("Zone-A");

        ClearanceResponse response = new ClearanceResponse();
        response.setId(1L);
        response.setPassengerId(1L);
        response.setCheckInId(5L);
        response.setStatus(ClearanceStatus.PENDING);
        response.setScreeningZone("Zone-A");
        response.setCreatedAt(LocalDateTime.now());
        response.setUpdatedAt(LocalDateTime.now());

        when(service.createClearance(any(CreateClearanceRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/clearances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.screeningZone").value("Zone-A"));
    }

    @Test
    void getClearance_notFound_returns404() throws Exception {
        when(service.getClearanceById(99L))
                .thenThrow(new NotFoundException("Clearance not found with id: 99"));

        mockMvc.perform(get("/api/v1/clearances/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Clearance not found with id: 99"));
    }
}