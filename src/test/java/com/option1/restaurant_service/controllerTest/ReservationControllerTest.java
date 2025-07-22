package com.option1.restaurant_service.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.option1.restaurant_service.dto.request.ConfirmReservationRequest;
import com.option1.restaurant_service.dto.request.ReservationRequest;
import com.option1.restaurant_service.dto.response.ReservationResponse;
import com.option1.restaurant_service.service.restaurantService.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class ReservationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    private ReservationRequest reservationRequest;
    private ReservationResponse reservationResponse;

    @BeforeEach
    void setup() {
        reservationRequest = ReservationRequest.builder()
            .reservationTime(LocalDateTime.of(2025, 5, 30, 19, 0))
            .quantityPeople(4)
            .restaurant("Res01")
            .build();

        reservationResponse = ReservationResponse.builder()
            .id("Resv01")
            .requestTime(LocalDateTime.of(2025, 5, 28, 14, 30))
            .reservationTime(LocalDateTime.of(2025, 5, 30, 19, 0))
            .quantityPeople(4)
            .restaurant("Res01")
            .user("User01")
            .status("UNCONFIRMED")
            .table(null)
            .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createReservation_success() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        String content = objectMapper.writeValueAsString(reservationRequest);

        when(reservationService.createReservation(ArgumentMatchers.any())).thenReturn(reservationResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
            .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("Resv01"))
            .andExpect(MockMvcResultMatchers.jsonPath("result.status").value("UNCONFIRMED"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllReservation_success() throws Exception {
        when(reservationService.getAllReservation()).thenReturn(List.of(reservationResponse));

        mockMvc.perform(MockMvcRequestBuilders.get("/reservation")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
            .andExpect(MockMvcResultMatchers.jsonPath("result[0].id").value("Resv01"))
            .andExpect(MockMvcResultMatchers.jsonPath("result[0].status").value("UNCONFIRMED"));
    }

    @Test
    @WithMockUser
    void getReservationByUserId_success() throws Exception {
        when(reservationService.getReservationByUserId()).thenReturn(List.of(reservationResponse));

        mockMvc.perform(MockMvcRequestBuilders.get("/reservation/history")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
            .andExpect(MockMvcResultMatchers.jsonPath("result[0].id").value("Resv01"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void confirmReservation_success() throws Exception {
        ConfirmReservationRequest confirmRequest = ConfirmReservationRequest.builder()
            .table("Table01")
            .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(confirmRequest);

        reservationResponse.setStatus("CONFIRMED");
        when(reservationService.confirmReservation(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
            .thenReturn(reservationResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/reservation/confirm/{reservationId}", "Resv01")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
            .andExpect(MockMvcResultMatchers.jsonPath("result.status").value("CONFIRMED"));
    }

    @Test
    @WithMockUser
    void cancleReservation_success() throws Exception {
        when(reservationService.cancleReservation("Resv01")).thenReturn("Reservation cancelled");

        mockMvc.perform(MockMvcRequestBuilders.put("/reservation/cancle/{reservationId}", "Resv01")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
            .andExpect(MockMvcResultMatchers.jsonPath("result").value("Reservation cancelled"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCancleReservation_success() throws Exception {
        when(reservationService.adminCancleReservation("Resv01")).thenReturn("Reservation cancelled by admin");

        mockMvc.perform(MockMvcRequestBuilders.put("/reservation/admin-cancle/{reservationId}", "Resv01")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
            .andExpect(MockMvcResultMatchers.jsonPath("result").value("Reservation cancelled by admin"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void gotTable_success() throws Exception {
        reservationResponse.setStatus("GOT TABLE");
        when(reservationService.gotTable("Resv01")).thenReturn(reservationResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/reservation/gotTable/{reservationId}", "Resv01")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
            .andExpect(MockMvcResultMatchers.jsonPath("result.status").value("GOT TABLE"));
    }
}
