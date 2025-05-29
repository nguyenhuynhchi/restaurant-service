package com.option1.restaurant_service.serviceTest;

import com.option1.restaurant_service.dto.request.RestaurantRequest;
import com.option1.restaurant_service.dto.response.RestaurantResponse;
import com.option1.restaurant_service.entity.DiningTable;
import com.option1.restaurant_service.entity.Restaurant;
import com.option1.restaurant_service.mapper.RestaurantMapper;
import com.option1.restaurant_service.repository.RestaurantRepository;
import com.option1.restaurant_service.repository.TableRepository;
import com.option1.restaurant_service.service.restaurantService.RestaurantService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.assertj.core.api.Assertions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@SpringBootTest
@TestPropertySource("/test.properties")
public class RestaurantServiceTest {
    @MockBean
    private RestaurantRepository restaurantRepository;

    @MockBean
    private TableRepository tableRepository;

    @MockBean
    private RestaurantMapper restaurantMapper;

    @Autowired
    private RestaurantService restaurantService;

    private RestaurantRequest restaurantRequest;
    private Restaurant restaurant;
    private RestaurantResponse restaurantResponse;
    private DiningTable table1;
    private DiningTable table2;

    @BeforeEach
    void init() {
        table1 = DiningTable.builder()
            .id("T01")
            .capacity(4)
            .description("Window Table")
            .build();

        table2 = DiningTable.builder()
            .id("T02")
            .capacity(6)
            .description("VIP Table")
            .build();

        restaurantRequest = RestaurantRequest.builder()
            .id("Res01")
            .name("Restaurant 1")
            .address("123 Main St")
            .phone("123-456-7890")
            .email("contact@restaurant1.com")
            .tables(Set.of("T01", "T02"))
            .build();

        restaurant = Restaurant.builder()
            .id("Res01")
            .name("Restaurant 1")
            .address("123 Main St")
            .phone("123-456-7890")
            .email("contact@restaurant1.com")
            .tables(new HashSet<>(List.of(table1, table2)))
            .build();

        restaurantResponse = RestaurantResponse.builder()
            .id("Res01")
            .name("Restaurant 1")
            .address("123 Main St")
            .phone("123-456-7890")
            .email("contact@restaurant1.com")
            .tables(Set.of())
            .build();
    }

    @Test
    void createRestaurant_success() {
        // Mock mapper
        when(restaurantMapper.toRestaurant(any(RestaurantRequest.class))).thenReturn(restaurant);

        // Mock repository
        when(tableRepository.findAllById(anyList())).thenReturn(List.of(table1, table2));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);
        when(restaurantMapper.toRestaurantResponse(any(Restaurant.class))).thenReturn(restaurantResponse);

        RestaurantResponse result = restaurantService.createRestaurant(restaurantRequest);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo("Res01");
        Assertions.assertThat(result.getName()).isEqualTo("Restaurant 1");
        Assertions.assertThat(result.getAddress()).isEqualTo("123 Main St");
    }

    @Test
    void getAllRestaurants_success() {
        // Mock repository
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant));
        when(restaurantMapper.toRestaurantResponse(any(Restaurant.class))).thenReturn(restaurantResponse);

        List<RestaurantResponse> result = restaurantService.getAllRestaurant();

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).hasSize(1);
        Assertions.assertThat(result.get(0).getId()).isEqualTo("Res01");
        Assertions.assertThat(result.get(0).getName()).isEqualTo("Restaurant 1");
    }

    @Test
    void deleteRestaurant_success() {
        // Mock repository
        doNothing().when(restaurantRepository).deleteById(anyString());

        restaurantService.deleteRestaurant("Res01");

        Assertions.assertThatNoException();
    }
}
