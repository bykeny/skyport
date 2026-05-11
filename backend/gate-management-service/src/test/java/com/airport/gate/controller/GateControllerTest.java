package com.airport.gate.controller;

import com.airport.gate.dto.UpdateGateStatusRequest;
import com.airport.gate.exception.GlobalExceptionHandler;
import com.airport.gate.exception.NotFoundException;
import com.airport.gate.model.Gate;
import com.airport.gate.model.GateStatus;
import com.airport.gate.model.GateType;
import com.airport.gate.service.GateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GateController.class)
@Import(GlobalExceptionHandler.class)
class GateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GateService gateService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void list_returnsAllGates() throws Exception {
        Gate g1 = buildGate(1L, "A1", "T1", GateType.NARROW_BODY, GateStatus.AVAILABLE);
        Gate g2 = buildGate(2L, "B2", "T2", GateType.WIDE_BODY, GateStatus.MAINTENANCE);
        when(gateService.list()).thenReturn(List.of(g1, g2));

        mockMvc.perform(get("/api/v1/gates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].gateNumber", is("A1")))
                .andExpect(jsonPath("$[0].status", is("AVAILABLE")))
                .andExpect(jsonPath("$[1].gateNumber", is("B2")))
                .andExpect(jsonPath("$[1].status", is("MAINTENANCE")));
    }

    @Test
    void get_returnsGateDetails() throws Exception {
        Gate g = buildGate(5L, "C3", "T1", GateType.REGIONAL, GateStatus.AVAILABLE);
        when(gateService.get(5L)).thenReturn(g);

        mockMvc.perform(get("/api/v1/gates/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.gateNumber", is("C3")))
                .andExpect(jsonPath("$.terminal", is("T1")));
    }

    @Test
    void get_notFound_returns404() throws Exception {
        when(gateService.get(999L)).thenThrow(new NotFoundException("Gate 999 not found"));

        mockMvc.perform(get("/api/v1/gates/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Gate 999 not found")));
    }

    @Test
    void updateStatus_returnsUpdatedGate() throws Exception {
        Gate g = buildGate(10L, "D4", "T2", GateType.WIDE_BODY, GateStatus.MAINTENANCE);
        when(gateService.updateStatus(10L, GateStatus.MAINTENANCE)).thenReturn(g);

        UpdateGateStatusRequest req = new UpdateGateStatusRequest();
        req.setStatus(GateStatus.MAINTENANCE);

        mockMvc.perform(patch("/api/v1/gates/10/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gateNumber", is("D4")))
                .andExpect(jsonPath("$.status", is("MAINTENANCE")));
    }

    private Gate buildGate(long id, String number, String terminal, GateType type, GateStatus status)
            throws Exception {
        Gate g = new Gate();
        Field idField = Gate.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(g, id);
        g.setGateNumber(number);
        g.setTerminal(terminal);
        g.setGateType(type);
        g.setStatus(status);
        return g;
    }
}
