package com.option1.restaurant_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class TableRequest {
    String id;          // Dạng id của nhà hàng: R<số nhà hàng>.<số bàn>. R01.01
    int capacity;
    String description; // Bàn gần cửa sổ,
}
