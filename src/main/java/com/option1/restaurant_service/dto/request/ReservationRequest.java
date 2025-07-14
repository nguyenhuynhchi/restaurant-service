package com.option1.restaurant_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationRequest {

    @Schema(description = "Thời gian nhận bàn", example = "2025-07-08T08:45")
    LocalDateTime reservationTime;  // Thời gian khách muốn đến nhận bàn

    @Schema(description = "Số lượng người", example = "4")
    int quantityPeople;

    @Schema(description = "Mã chi nhánh nhà hàng", example ="R2")
    String restaurant;

    @Schema(description = "Tin nhắn ghi chú", example ="Tôi muốn bàn ngoài trời")
    String messenger;
}
