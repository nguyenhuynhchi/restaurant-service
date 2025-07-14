package com.option1.restaurant_service.dto.request;

import com.option1.restaurant_service.validation.DobConstraint;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // Dể dàng tạo các Object
@FieldDefaults(level = AccessLevel.PRIVATE) // Mặc định quyền cho các trường(Field) là private
public class UserCreationRequest {

    @NotBlank(message = "USERNAME_INVALID")
    @Size(min = 3, max = 50, message = "USERNAME_LENGTH_INVALID")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "USERNAME_INVALID")
    @Schema(description = "Tên đăng nhập", example = "john_doe")
    String username;

    @NotBlank(message = "FULLNAME_INVALID")
    @Pattern(regexp = "^[\\p{L} ]+$", message = "FULLNAME_INVALID")
    @Schema(description = "Họ và tên", example = "Nguyễn Văn A")
    String fullname;

    @Size(min = 8, message = "USERPASSWORD_INVALID")
    @Schema(description = "Mật khẩu", example = "P@ssw0rd123")
    String password;

    @Email(message = "EMAIL_INVALID")
    @Schema(description = "Email", example = "user@example.com")
    String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "PHONE_INVALID") // phải là số từ 0 - 9 và phải có đúng 10 kí số
    @Schema(description = "Số điện thoại", example = "0123456789")
    String phone;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    @Schema(description = "Ngày sinh", example = "2000-01-01")
    LocalDate dob;

    @Schema(description = "Giới tính", example = "male")
    String sex;

    @Schema(description = "Vai trò", example = "[\"USER\"]")
    List<String> roles;
}
