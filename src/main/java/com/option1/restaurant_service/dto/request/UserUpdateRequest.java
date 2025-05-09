package com.option1.restaurant_service.dto.request;

import com.option1.restaurant_service.validation.DobConstraint;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // Dể dàng tạo các Object
@FieldDefaults(level = AccessLevel.PRIVATE) // Mặc định quyền cho các trường(Field) là private
public class UserUpdateRequest {

    @Size(min = 3, max = 50, message = "USERNAME_LENGTH_INVALID")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "USERNAME_INVALID")
    String username;
    @Pattern(regexp = "^[a-zA-Z]*$", message = "FULLNAME_INVALID")
    String fullname;
    @Size(min = 8, message = "USERPASSWORD_INVALID")
    String password;
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "EMAIL_INVALID")
    String email;
    @Pattern(regexp = "^[0-9]{10}$", message = "PHONE_INVALID")
    // phải là số từ 0 - 9 và phải có đúng 10 kí số
    String phone;
    @DobConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dob;
    String sex;

    List<String> roles;
}
