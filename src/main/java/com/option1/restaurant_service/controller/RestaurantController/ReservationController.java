package com.option1.restaurant_service.controller.RestaurantController;

import com.option1.restaurant_service.dto.request.ConfirmReservationRequest;
import com.option1.restaurant_service.dto.request.ReservationRequest;
import com.option1.restaurant_service.dto.response.ApiResponse;
import com.option1.restaurant_service.dto.response.ReservationResponse;
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
    ApiResponse<ReservationResponse> createReservation(@RequestBody ReservationRequest request) {
        return ApiResponse.<ReservationResponse>builder()
            .result(reservationService.createReservation(request))
            .build();
    }


    @GetMapping("/reservation")
    ApiResponse<List<ReservationResponse>> getAllReservation() {
        return ApiResponse.<List<ReservationResponse>>builder()
            .result(reservationService.getAllReservation())
            .build();
    }

    @GetMapping("/reservation-history")
    ApiResponse<List<ReservationResponse>> getReservationByUserId() {
        return ApiResponse.<List<ReservationResponse>>builder()
            .result(reservationService.getReservationByUserId())
            .build();
    }

    @PutMapping("/reservation/{reservationId}")
    ApiResponse<ReservationResponse> confirmReservation(@PathVariable String reservationId,
        @RequestBody ConfirmReservationRequest request) {
        return ApiResponse.<ReservationResponse>builder()
            .result(reservationService.confirmReservation(reservationId, request))
            .build();
    }

    @PutMapping("/reservation-cancle/{reservationId}")
    ApiResponse<String> cancleReservation(@PathVariable String reservationId){
        return ApiResponse.<String>builder()
            .result(reservationService.cancleReservation(reservationId))
            .build();
    }


    @PutMapping("/reservation-admin-cancle/{reservationId}")
    ApiResponse<String> adminCancleReservation(@PathVariable String reservationId){
        return ApiResponse.<String>builder()
            .result(reservationService.adminCancleReservation(reservationId))
            .build();
    }

    @PutMapping("/reservation-gotTable/{reservationId}")
    ApiResponse<ReservationResponse> gotTable(@PathVariable String reservationId){
        return ApiResponse.<ReservationResponse>builder()
            .result(reservationService.gotTable(reservationId))
            .build();
    }
}
