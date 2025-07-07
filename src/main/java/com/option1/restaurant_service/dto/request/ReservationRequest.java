package com.option1.restaurant_service.dto.request;

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

    LocalDateTime reservationTime;  // Thời gian khách muốn đến nhận bàn
    int quantityPeople;
    String restaurant;
    String messenger;
}
