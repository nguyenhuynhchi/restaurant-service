package com.option1.restaurant_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // Dể dàng tạo các Object
@FieldDefaults(level = AccessLevel.PRIVATE) // Mặc định quyền cho các trường(Field) là private
public class AuthenticationRequest {
    String username;
    String password;
}
