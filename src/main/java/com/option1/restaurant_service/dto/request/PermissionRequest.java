package com.option1.restaurant_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // Dể dàng tạo các Object
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionRequest {
  String name;
  String description;
}
