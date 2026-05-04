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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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
    void assign_returns201_andBody() throws Exception {
        Gate gate = new Gate();
        setId(gate, 7L);
        gate.setGateNumber("A7");
        gate.setTerminal("T1");
        gate.setStatus(GateStatus.OCCUPIED);

        GateAssignment assignment = new GateAssignment();
        setId(assignment, 123L);
        assignment.setGate(gate);
        assignment.setFlightId(555L);

        when(service.assign(any(AssignGateRequest.class))).thenReturn(assignment);

        String body = """
          { "gateId": 7, "flightId": 555 }
        """;

        mockMvc.perform(post("/api/v1/gate-assignments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/gate-assignments/123"))
                .andExpect(jsonPath("$.gateNumber", is("A7")))
                .andExpect(jsonPath("$.flightId", is(555)));
    }

    @Test
    void get_notFound_returns404() throws Exception {
        when(service.get(999L)).thenThrow(new NotFoundException("GateAssignment 999 not found"));

        mockMvc.perform(get("/api/v1/gate-assignments/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("GateAssignment 999 not found")));
    }

    @Test
    void delete_release_returns204() throws Exception {
        doNothing().when(service).release(123L);

        mockMvc.perform(delete("/api/v1/gate-assignments/123"))
                .andExpect(status().isNoContent());
    }

    private static void setId(Gate g, long id) throws Exception {
        Field idField = Gate.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(g, id);
    }

    private static void setId(GateAssignment a, long id) throws Exception {
        Field idField = GateAssignment.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(a, id);
    }
}
