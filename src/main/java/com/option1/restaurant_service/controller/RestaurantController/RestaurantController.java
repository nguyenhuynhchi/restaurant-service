package com.option1.restaurant_service.controller.RestaurantController;

import com.option1.restaurant_service.dto.response.ApiResponse;
import com.option1.restaurant_service.dto.request.RestaurantRequest;
import com.option1.restaurant_service.dto.response.RestaurantResponse;
import com.option1.restaurant_service.service.restaurantService.RestaurantService;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/restaurant")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RestaurantController {
    RestaurantService restaurantService;

    @PostMapping
    ApiResponse<RestaurantResponse> createRestaurant(@RequestBody RestaurantRequest request){
        return ApiResponse.<RestaurantResponse>builder()
            .result(restaurantService.createRestaurant(request))
            .build();
    }

    @GetMapping
    ApiResponse<List<RestaurantResponse>> getAllRestaurants(){
        return ApiResponse.<List<RestaurantResponse>>builder()
            .result(restaurantService.getAllRestaurant())
            .build();
    }

    @DeleteMapping("/delete/{idRestaurant}")
    ApiResponse<String> deleteRestaurant(@PathVariable String idRestaurant){
        restaurantService.deleteRestaurant(idRestaurant);
        return ApiResponse.<String>builder()
            .result("Have been delete restaurant").build();
    }
}
