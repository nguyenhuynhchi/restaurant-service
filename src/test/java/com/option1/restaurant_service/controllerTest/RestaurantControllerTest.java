package com.option1.restaurant_service.controllerTest;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.option1.restaurant_service.dto.request.RestaurantRequest;
import com.option1.restaurant_service.dto.response.RestaurantResponse;
import com.option1.restaurant_service.dto.response.TableResponse;
import com.option1.restaurant_service.service.restaurantService.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class RestaurantControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    private RestaurantRequest restaurantRequest;
    private RestaurantResponse restaurantResponse;

    @BeforeEach
    void init() {
        TableResponse tableResponse = TableResponse.builder()
            .id("R01.01")
            .capacity(4)
            .description("Table near the window")
            .build();

        restaurantRequest = RestaurantRequest.builder()
            .id("Res01")
            .name("Option1 Restaurant")
            .address("123 Main St")
            .phone("123456789")
            .email("contact@option1.com")
            .tables(Set.of("R01.01"))
            .build();

        restaurantResponse = RestaurantResponse.builder()
            .id("Res01")
            .name("Option1 Restaurant")
            .address("123 Main St")
            .phone("123456789")
            .email("contact@option1.com")
            .tables(Set.of(tableResponse))
            .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createRestaurant_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(restaurantRequest);
        when(restaurantService.createRestaurant(ArgumentMatchers.any())).thenReturn(restaurantResponse);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/restaurant")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
            .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("Res01"))
            .andExpect(MockMvcResultMatchers.jsonPath("result.name").value("Option1 Restaurant"))
            .andExpect(MockMvcResultMatchers.jsonPath("result.address").value("123 Main St"))
            .andExpect(MockMvcResultMatchers.jsonPath("result.phone").value("123456789"))
            .andExpect(MockMvcResultMatchers.jsonPath("result.email").value("contact@option1.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("result.tables[0].id").value("R01.01"))
            .andExpect(MockMvcResultMatchers.jsonPath("result.tables[0].capacity").value(4))
            .andExpect(MockMvcResultMatchers.jsonPath("result.tables[0].description").value("Table near the window"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllRestaurants_success() throws Exception {
        // GIVEN
        List<RestaurantResponse> restaurants = List.of(restaurantResponse);
        when(restaurantService.getAllRestaurant()).thenReturn(restaurants);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/restaurant")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
            .andExpect(MockMvcResultMatchers.jsonPath("result[0].id").value("Res01"))
            .andExpect(MockMvcResultMatchers.jsonPath("result[0].name").value("Option1 Restaurant"))
            .andExpect(MockMvcResultMatchers.jsonPath("result[0].address").value("123 Main St"))
            .andExpect(MockMvcResultMatchers.jsonPath("result[0].phone").value("123456789"))
            .andExpect(MockMvcResultMatchers.jsonPath("result[0].email").value("contact@option1.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("result[0].tables[0].id").value("R01.01"))
            .andExpect(MockMvcResultMatchers.jsonPath("result[0].tables[0].capacity").value(4))
            .andExpect(MockMvcResultMatchers.jsonPath("result[0].tables[0].description").value("Table near the window"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteRestaurant_success() throws Exception {
        // GIVEN
        String restaurantId = "Res01";
        doNothing().when(restaurantService).deleteRestaurant(restaurantId);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/restaurant/delete/{idRestaurant}", restaurantId)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
            .andExpect(MockMvcResultMatchers.jsonPath("result").value("Have been delete restaurant"));
    }
}
