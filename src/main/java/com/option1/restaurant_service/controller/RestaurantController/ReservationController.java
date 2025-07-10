package com.option1.restaurant_service.controller.RestaurantController;

import com.option1.restaurant_service.dto.request.ConfirmReservationRequest;
import com.option1.restaurant_service.dto.request.ReservationFilterRequest;
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
@RequestMapping("/reservation")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ReservationController {

    ReservationService reservationService;

    @PostMapping
    ApiResponse<ReservationResponse> createReservation(@RequestBody ReservationRequest request) {
        return ApiResponse.<ReservationResponse>builder()
            .result(reservationService.createReservation(request))
            .build();
    }

    @GetMapping
    ApiResponse<List<ReservationResponse>> getAllReservation() {
        return ApiResponse.<List<ReservationResponse>>builder()
            .result(reservationService.getAllReservation())
            .build();
    }

    @GetMapping("/history")
    ApiResponse<List<ReservationResponse>> getReservationByUserId() {
        return ApiResponse.<List<ReservationResponse>>builder()
            .result(reservationService.getReservationByUserId())
            .build();
    }

    @GetMapping("/coming")
    ApiResponse<List<ReservationResponse>> getReservationComing() {
        return ApiResponse.<List<ReservationResponse>>builder()
            .result(reservationService.getReservationComing())
            .build();
    }

    @GetMapping("/unconfirmed")
    ApiResponse<List<ReservationResponse>> getReservationUnconfirmed(){return ApiResponse.<List<ReservationResponse>>builder()
            .result(reservationService.getReservationUnconfirmed())
            .build();
    }

    @PostMapping("/filter")
    ApiResponse<List<ReservationResponse>> getReservationFilter(@RequestBody ReservationFilterRequest request){
        return ApiResponse.<List<ReservationResponse>>builder()
            .result(reservationService.getReservationFilter(request))
            .build();
    }

    @PutMapping("/confirm/{reservationId}") // QUản lý xác nhận đặt bàn
    ApiResponse<ReservationResponse> confirmReservation(@PathVariable String reservationId,
        @RequestBody ConfirmReservationRequest request) {
        return ApiResponse.<ReservationResponse>builder()
            .result(reservationService.confirmReservation(reservationId, request))
            .build();
    }

    @PutMapping("/cancle/{reservationId}") // Khách hủy bàn
    ApiResponse<String> cancleReservation(@PathVariable String reservationId){
        return ApiResponse.<String>builder()
            .result(reservationService.cancleReservation(reservationId))
            .build();
    }


    @PutMapping("/admin-cancle/{reservationId}")  // Admin hủy bàn (User quá hạn
    ApiResponse<String> adminCancleReservation(@PathVariable String reservationId){
        return ApiResponse.<String>builder()
            .result(reservationService.adminCancleReservation(reservationId))
            .build();
    }

    @PutMapping("/gotTable/{reservationId}")  // Khách nhận bàn
    ApiResponse<ReservationResponse> gotTable(@PathVariable String reservationId){
        return ApiResponse.<ReservationResponse>builder()
            .result(reservationService.gotTable(reservationId))
            .build();
    }
}
