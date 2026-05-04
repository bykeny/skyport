package com.airport.notification.controller;

import com.airport.notification.domain.NotificationChannel;
import com.airport.notification.domain.NotificationStatus;
import com.airport.notification.domain.RecipientType;
import com.airport.notification.dto.NotificationResponse;
import com.airport.notification.dto.SendNotificationRequest;
import com.airport.notification.exception.GlobalExceptionHandler;
import com.airport.notification.exception.NotFoundException;
import com.airport.notification.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
@Import(GlobalExceptionHandler.class)
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void sendNotification_happyPath_returns201() throws Exception {
        SendNotificationRequest request = new SendNotificationRequest();
        request.setRecipientId(1L);
        request.setRecipientType(RecipientType.PASSENGER);
        request.setChannel(NotificationChannel.EMAIL);
        request.setSubject("Gate changed");
        request.setBody("Your flight gate has changed to A12.");

        NotificationResponse response = new NotificationResponse();
        response.setId(1L);
        response.setRecipientId(1L);
        response.setRecipientType(RecipientType.PASSENGER);
        response.setChannel(NotificationChannel.EMAIL);
        response.setSubject("Gate changed");
        response.setBody("Your flight gate has changed to A12.");
        response.setStatus(NotificationStatus.SENT);
        response.setSentAt(LocalDateTime.now());

        when(notificationService.sendNotification(any(SendNotificationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.recipientId").value(1L))
                .andExpect(jsonPath("$.channel").value("EMAIL"))
                .andExpect(jsonPath("$.status").value("SENT"));
    }

    @Test
    void getNotification_notFound_returns404() throws Exception {
        when(notificationService.getNotification(99L))
                .thenThrow(new NotFoundException("Notification not found with id: 99"));

        mockMvc.perform(get("/api/v1/notifications/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Notification not found with id: 99"));
    }
}
