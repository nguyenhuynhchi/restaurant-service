package com.option1.restaurant_service.dto.response;

import java.time.LocalDate;


import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // Dể dàng tạo các Object
@FieldDefaults(level = AccessLevel.PRIVATE) // Mặc định quyền cho các trường(Field) là private
public class UserResponse {
  String id;
  String username;
  String fullname;
  String password;
  String email;
  String phone;
  LocalDate dob;
  String sex;

  Set<RoleResponse> roles;
}
