package com.option1.restaurant_service.dto.response;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationResponse {

    String id;
    LocalDateTime requestTime;      // Thời gian khách gửi yêu cầu đặt bàn
    LocalDateTime reservationTime;  // Thời gian khách muốn đến nhận bàn
    int quantityPeople;
    String restaurant;
    String user;
    String status;
    String table;
    String messenger;
}
