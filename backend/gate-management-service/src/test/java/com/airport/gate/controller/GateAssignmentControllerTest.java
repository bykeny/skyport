package com.airport.gate.controller;

import com.airport.gate.dto.AssignGateRequest;
import com.airport.gate.exception.GlobalExceptionHandler;
import com.airport.gate.exception.NotFoundException;
import com.airport.gate.model.Gate;
import com.airport.gate.model.GateAssignment;
import com.airport.gate.model.GateStatus;
import com.airport.gate.service.GateAssignmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.lang.reflect.Field;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GateAssignmentController.class)
@Import(GlobalExceptionHandler.class)
class GateAssignmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GateAssignmentService service;

    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void assign_returns201_withLocationHeader() throws Exception {
        Gate gate = buildGate(7L, "A7", "T1", GateStatus.OCCUPIED);
        GateAssignment assignment = buildAssignment(123L, gate, 555L);
        when(service.assign(any(AssignGateRequest.class))).thenReturn(assignment);

        String body = """
                { "gateId": 7, "flightId": 555 }
                """;

        mockMvc.perform(post("/api/v1/gate-assignments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/gate-assignments/123"))
                .andExpect(jsonPath("$.id", is(123)))
                .andExpect(jsonPath("$.gateNumber", is("A7")))
                .andExpect(jsonPath("$.flightId", is(555)));
    }

    @Test
    void assign_missingRequiredFields_returns400() throws Exception {
        String body = """
                { "gateId": 7 }
                """;

        mockMvc.perform(post("/api/v1/gate-assignments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void get_returnsAssignment() throws Exception {
        Gate gate = buildGate(3L, "B3", "T2", GateStatus.OCCUPIED);
        GateAssignment assignment = buildAssignment(50L, gate, 200L);
        when(service.get(50L)).thenReturn(assignment);

        mockMvc.perform(get("/api/v1/gate-assignments/50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(50)))
                .andExpect(jsonPath("$.flightId", is(200)));
    }

    @Test
    void get_notFound_returns404() throws Exception {
        when(service.get(999L)).thenThrow(new NotFoundException("GateAssignment 999 not found"));

        mockMvc.perform(get("/api/v1/gate-assignments/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("GateAssignment 999 not found")));
    }

    @Test
    void release_returns204() throws Exception {
        doNothing().when(service).release(123L);

        mockMvc.perform(delete("/api/v1/gate-assignments/123"))
                .andExpect(status().isNoContent());
    }

    @Test
    void release_alreadyReleased_returns409() throws Exception {
        doThrow(new IllegalStateException("Gate assignment has already been released"))
                .when(service).release(123L);

        mockMvc.perform(delete("/api/v1/gate-assignments/123"))
                .andExpect(status().isConflict());
    }

    @Test
    void getByFlight_returnsActiveAssignment() throws Exception {
        Gate gate = buildGate(4L, "C4", "T1", GateStatus.OCCUPIED);
        GateAssignment assignment = buildAssignment(77L, gate, 300L);
        when(service.getByFlight(300L)).thenReturn(assignment);

        mockMvc.perform(get("/api/v1/gate-assignments/flight/300"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightId", is(300)))
                .andExpect(jsonPath("$.gateNumber", is("C4")));
    }

    private Gate buildGate(long id, String number, String terminal, GateStatus status) throws Exception {
        Gate g = new Gate();
        Field f = Gate.class.getDeclaredField("id");
        f.setAccessible(true);
        f.set(g, id);
        g.setGateNumber(number);
        g.setTerminal(terminal);
        g.setStatus(status);
        return g;
    }

    private GateAssignment buildAssignment(long id, Gate gate, long flightId) throws Exception {
        GateAssignment a = new GateAssignment();
        Field f = GateAssignment.class.getDeclaredField("id");
        f.setAccessible(true);
        f.set(a, id);
        a.setGate(gate);
        a.setFlightId(flightId);
        return a;
    }
}
