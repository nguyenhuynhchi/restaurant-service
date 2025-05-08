package com.option1.restaurant_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // Dể dàng tạo các Object
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionResponse {

  String name;
  String description;
}
