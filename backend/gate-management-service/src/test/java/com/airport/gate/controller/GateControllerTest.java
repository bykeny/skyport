package com.airport.gate.controller;

import com.airport.gate.exception.GlobalExceptionHandler;
import com.airport.gate.exception.NotFoundException;
import com.airport.gate.model.Gate;
import com.airport.gate.model.GateStatus;
import com.airport.gate.model.GateType;
import com.airport.gate.service.GateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.util.List;

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

    @Test
    void list_returnsGates() throws Exception {
        Gate g1 = new Gate(); setId(g1, 1L); g1.setGateNumber("A1"); g1.setTerminal("T1"); g1.setGateType(GateType.NARROW_BODY); g1.setStatus(GateStatus.AVAILABLE);
        Gate g2 = new Gate(); setId(g2, 2L); g2.setGateNumber("B2"); g2.setTerminal("T1"); g2.setGateType(GateType.WIDE_BODY); g2.setStatus(GateStatus.MAINTENANCE);
        when(gateService.list()).thenReturn(List.of(g1, g2));

        mockMvc.perform(get("/api/v1/gates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].gateNumber", is("A1")))
                .andExpect(jsonPath("$[1].gateNumber", is("B2")));
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
        Gate g = new Gate(); setId(g, 10L); g.setGateNumber("C3"); g.setTerminal("T2"); g.setStatus(GateStatus.AVAILABLE);
        when(gateService.updateStatus(10L, GateStatus.OCCUPIED)).thenReturn(g);

        mockMvc.perform(patch("/api/v1/gates/10/status").param("status", "OCCUPIED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gateNumber", is("C3")));
    }

    private static void setId(Gate g, long id) throws Exception {
        Field idField = Gate.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(g, id);
    }
}
