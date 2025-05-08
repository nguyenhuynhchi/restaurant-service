package com.option1.restaurant_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // Dể dàng tạo các Object
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {

  String name;
  String description;
  Set<PermissionResponse> permissions;
}
