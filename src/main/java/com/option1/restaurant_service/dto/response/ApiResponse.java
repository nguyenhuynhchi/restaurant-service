package com.option1.restaurant_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // Dể dàng tạo các Object
@FieldDefaults(level = AccessLevel.PRIVATE) // Mặc định quyền cho các trường(Field) là private
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ApiResponse<T> {

    @Builder.Default
    int code = 1000;
    String message;
    T result;
}
