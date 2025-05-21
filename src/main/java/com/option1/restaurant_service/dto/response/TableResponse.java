package com.option1.restaurant_service.dto.response;

import com.option1.restaurant_service.entity.Reservation;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TableResponse {
    String id;
    int capacity;
    String description;
}
