package com.option1.restaurant_service.controller.RestaurantController;

import com.option1.restaurant_service.dto.request.ReservationRequest;
import com.option1.restaurant_service.dto.response.ApiResponse;
import com.option1.restaurant_service.dto.response.ReservationResponse;
import com.option1.restaurant_service.entity.Reservation;
import com.option1.restaurant_service.service.restaurantService.ReservationService;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/info-restaurant")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ReservationController {

    ReservationService reservationService;

    @PostMapping("/reservation")
    ApiResponse<ReservationResponse> createReservation(@RequestBody ReservationRequest request){
        return ApiResponse.<ReservationResponse>builder()
            .result(reservationService.createReservation(request))
            .build();
    }


    @GetMapping("/reservation")
    ApiResponse<List<ReservationResponse>> getAllReservation(){
        return ApiResponse.<List<ReservationResponse>>builder()
            .result(reservationService.getAllReservation())
            .build();
    }
}
