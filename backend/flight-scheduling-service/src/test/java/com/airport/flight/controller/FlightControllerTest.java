package com.airport.flight.controller;

import com.airport.flight.dto.CreateFlightRequest;
import com.airport.flight.exception.GlobalExceptionHandler;
import com.airport.flight.exception.NotFoundException;
import com.airport.flight.model.Flight;
import com.airport.flight.model.FlightStatus;
import com.airport.flight.service.FlightService;
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

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FlightController.class)
@Import(GlobalExceptionHandler.class)
class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightService flightService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void create_returns201_andBody() throws Exception {
        Flight created = new Flight();
        setId(created, 100L);
        created.setFlightNumber("XY789");
        created.setOrigin("TLL");
        created.setDestination("RIX");
        created.setScheduledDeparture(LocalDateTime.of(2099, 1, 1, 10, 0));
        created.setScheduledArrival(LocalDateTime.of(2099, 1, 1, 12, 0));
        created.setStatus(FlightStatus.SCHEDULED);
        when(flightService.create(any(CreateFlightRequest.class))).thenReturn(created);

        String body = """
          {
            "flightNumber":"XY789",
            "origin":"TLL",
            "destination":"RIX",
            "scheduledDeparture":"2099-01-01T10:00:00",
            "scheduledArrival":"2099-01-01T12:00:00",
            "aircraftType":"A220",
            "airlineCode":"XY"
          }
        """;

        mockMvc.perform(post("/api/v1/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.flightNumber", is("XY789")))
                .andExpect(jsonPath("$.origin", is("TLL")))
                .andExpect(jsonPath("$.destination", is("RIX")));
    }

    @Test
    void get_notFound_returns404() throws Exception {
        when(flightService.get(999L)).thenThrow(new NotFoundException("Flight 999 not found"));

        mockMvc.perform(get("/api/v1/flights/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Flight 999 not found")));
    }

    private static void setId(Flight f, long id) throws Exception {
        Field idField = Flight.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(f, id);
    }
}
