package com.option1.restaurant_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class TableRequest {
    @Schema(description = "Mã bàn", example = "R1.01")
    String id;          // Dạng id của nhà hàng: R<số nhà hàng>.<số bàn>. R01.01

    @Schema(description = "Số lượng người", example = "2")
    int capacity;

    @Schema(description = "Mô tả đặc điểm bàn", example = "Bàn gần cửa sổ")
    String description; // Bàn gần cửa sổ,
}
