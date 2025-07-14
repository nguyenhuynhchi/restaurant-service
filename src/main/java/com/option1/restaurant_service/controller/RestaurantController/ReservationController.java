package com.option1.restaurant_service.controller.RestaurantController;

import com.option1.restaurant_service.dto.request.ConfirmReservationRequest;
import com.option1.restaurant_service.dto.request.ReservationFilterRequest;
import com.option1.restaurant_service.dto.request.ReservationRequest;
import com.option1.restaurant_service.dto.response.ApiResponse;
import com.option1.restaurant_service.dto.response.ReservationResponse;
import com.option1.restaurant_service.service.restaurantService.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Reservation API", description = "Quản lý đơn đặt bàn")
public class ReservationController {

    ReservationService reservationService;

    @Operation(
        summary = "Tạo đơn đặt bàn",
        description = "",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping
    ApiResponse<ReservationResponse> createReservation(@RequestBody ReservationRequest request) {
        return ApiResponse.<ReservationResponse>builder()
            .result(reservationService.createReservation(request))
            .build();
    }

    @Operation(
        summary = "Xem tất cả đơn đặt bàn",
        description = "Chỉ ADMIN mới có thể xem tất cả đơn đặt bàn",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    ApiResponse<List<ReservationResponse>> getAllReservation() {
        return ApiResponse.<List<ReservationResponse>>builder()
            .result(reservationService.getAllReservation())
            .build();
    }

    @Operation(
        summary = "Xem lịch sử bàn đã đặt",
        description = "Người dùng xem các bàn đã đặt",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/history")
    ApiResponse<List<ReservationResponse>> getReservationByUserId() {
        return ApiResponse.<List<ReservationResponse>>builder()
            .result(reservationService.getReservationByUserId())
            .build();
    }

    @Operation(
        summary = "Xem thông tin đặt bàn chưa đến giờ nhận",
        description = "",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/coming")
    ApiResponse<List<ReservationResponse>> getReservationComing() {
        return ApiResponse.<List<ReservationResponse>>builder()
            .result(reservationService.getReservationComing())
            .build();
    }

    @Operation(
        summary = "Xem các đơn đặt bàn chưa được xác nhận bàn",
        description = "Chỉ ADMIN mới có thể xem và để xác nhận bàn cho các đơn đặt bàn",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/unconfirmed")
    ApiResponse<List<ReservationResponse>> getReservationUnconfirmed(){return ApiResponse.<List<ReservationResponse>>builder()
            .result(reservationService.getReservationUnconfirmed())
            .build();
    }

    @Operation(
        summary = "Lọc các đơn đặt bàn chưa đến giờ nhận",
        description = "Chỉ ADMIN mới có thể xem. Dùng để xem các bàn đã được xác nhận và biết được các bàn trống để xác nhận các bàn còn lại",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/filter")
    ApiResponse<List<ReservationResponse>> getReservationFilter(@RequestBody ReservationFilterRequest request){
        return ApiResponse.<List<ReservationResponse>>builder()
            .result(reservationService.getReservationFilter(request))
            .build();
    }

    @Operation(
        summary = "Chọn bàn và xác nhận cho đơn đặt bàn",
        description = "ADMIN sau khi lọc để biết bàn trống thì chọn bàn và xác nhận bàn cho đơn đặt bàn",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/confirm/{reservationId}") // QUản lý xác nhận đặt bàn
    ApiResponse<ReservationResponse> confirmReservation(@PathVariable String reservationId,
        @RequestBody ConfirmReservationRequest request) {
        return ApiResponse.<ReservationResponse>builder()
            .result(reservationService.confirmReservation(reservationId, request))
            .build();
    }

    @Operation(
        summary = "Hủy bàn đã đặt",
        description = "Người dùng hủy bàn đã đặt. Nhưng chỉ được hủy trước thời gian nhận bàn 1 tiếng",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/cancle/{reservationId}") // Khách hủy bàn
    ApiResponse<String> cancleReservation(@PathVariable String reservationId){
        return ApiResponse.<String>builder()
            .result(reservationService.cancleReservation(reservationId))
            .build();
    }

    @Operation(
        summary = "Hủy bàn",
        description = "ADMIN hủy bàn",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin-cancle/{reservationId}")  // Admin hủy bàn (User quá hạn
    ApiResponse<String> adminCancleReservation(@PathVariable String reservationId){
        return ApiResponse.<String>builder()
            .result(reservationService.adminCancleReservation(reservationId))
            .build();
    }

    @Operation(
        summary = "Nhận bàn",
        description = "ADMIN cập nhật thông tin cho đơn đặt bàn là khách đã nhận bàn",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/gotTable/{reservationId}")  // Khách nhận bàn
    ApiResponse<ReservationResponse> gotTable(@PathVariable String reservationId){
        return ApiResponse.<ReservationResponse>builder()
            .result(reservationService.gotTable(reservationId))
            .build();
    }
}
