package com.airport.flight.controller;

import com.airport.flight.dto.CreateFlightRequest;
import com.airport.flight.exception.GlobalExceptionHandler;
import com.airport.flight.exception.NotFoundException;
import com.airport.flight.model.Flight;
import com.airport.flight.model.FlightStatus;
import com.airport.flight.service.FlightService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
        setId(created, 1L);
        created.setFlightNumber("TK101");
        created.setOrigin("TLL");
        created.setDestination("IST");
        created.setScheduledDeparture(LocalDateTime.of(2099, 6, 1, 10, 0));
        created.setScheduledArrival(LocalDateTime.of(2099, 6, 1, 14, 0));
        created.setAircraftType("B737");
        created.setAirlineCode("TK");
        when(flightService.create(any(CreateFlightRequest.class))).thenReturn(created);

        String body = """
                {
                  "flightNumber": "TK101",
                  "origin": "TLL",
                  "destination": "IST",
                  "scheduledDeparture": "2099-06-01T10:00:00",
                  "scheduledArrival": "2099-06-01T14:00:00",
                  "aircraftType": "B737",
                  "airlineCode": "TK"
                }
                """;

        mockMvc.perform(post("/api/v1/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/flights/1"))
                .andExpect(jsonPath("$.flightNumber", is("TK101")))
                .andExpect(jsonPath("$.origin", is("TLL")))
                .andExpect(jsonPath("$.destination", is("IST")))
                .andExpect(jsonPath("$.status", is("SCHEDULED")));
    }

    @Test
    void create_missingRequiredFields_returns400() throws Exception {
        String body = """
                { "flightNumber": "TK101" }
                """;

        mockMvc.perform(post("/api/v1/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void get_returnsFlightDetails() throws Exception {
        Flight f = new Flight();
        setId(f, 5L);
        f.setFlightNumber("LO202");
        f.setOrigin("WAW");
        f.setDestination("TLL");
        f.setScheduledDeparture(LocalDateTime.of(2099, 7, 1, 8, 0));
        f.setScheduledArrival(LocalDateTime.of(2099, 7, 1, 10, 0));
        when(flightService.get(5L)).thenReturn(f);

        mockMvc.perform(get("/api/v1/flights/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.flightNumber", is("LO202")));
    }

    @Test
    void get_notFound_returns404() throws Exception {
        when(flightService.get(999L)).thenThrow(new NotFoundException("Flight 999 not found"));

        mockMvc.perform(get("/api/v1/flights/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Flight 999 not found")));
    }

    @Test
    void cancel_returnsCancelledFlight() throws Exception {
        Flight f = new Flight();
        setId(f, 10L);
        f.setFlightNumber("AB300");
        f.setOrigin("RIX");
        f.setDestination("HEL");
        f.setScheduledDeparture(LocalDateTime.of(2099, 8, 1, 12, 0));
        f.setScheduledArrival(LocalDateTime.of(2099, 8, 1, 13, 0));
        f.setStatus(FlightStatus.CANCELLED);
        when(flightService.cancel(10L)).thenReturn(f);

        mockMvc.perform(post("/api/v1/flights/10/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("CANCELLED")));
    }

    private static void setId(Flight f, long id) throws Exception {
        Field idField = Flight.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(f, id);
    }
}
