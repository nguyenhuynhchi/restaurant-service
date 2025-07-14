package com.option1.restaurant_service.controller;

import com.option1.restaurant_service.dto.response.ApiResponse;
import com.option1.restaurant_service.dto.request.UserCreationRequest;
import com.option1.restaurant_service.dto.request.UserUpdateRequest;
import com.option1.restaurant_service.dto.response.UserResponse;
import com.option1.restaurant_service.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
@Slf4j
@Tag(name = "User API", description = "Quản lý người dùng trong hệ thống")
public class UserController {

    UserService userService;

    @Operation(
        summary = "Lấy danh sách người dùng",
        description = "Chỉ ADMIN mới được phép truy cập"
//        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Usename: {}", authentication.getName());
        authentication.getAuthorities()
            .forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return ApiResponse.<List<UserResponse>>builder()
            .result(userService.getUsers())
            .build();
    }

    @Operation(
        summary = "Tạo người dùng mới",
        description = "Đăng ký tạo một tài khoản mới"
    )
    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
            .result(userService.createUser(request)).build();
    }

    @Operation(
        summary = "Lấy thông tin 1 người dùng qua ID",
        description = ""
    )
    @GetMapping("/getByID/{userID}")
    ApiResponse<UserResponse> getUserID(@PathVariable("userID") String userID) {
        return ApiResponse.<UserResponse>builder()
            .result(userService.getUserID(userID))
            .build();
    }

    @Operation(
        summary = "Cập nhật thông tin người dùng",
        description = "Người dùng có thể chỉnh sửa thông tin của chính mình"
    )
    @PutMapping("/{userID}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userID,
        @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
            .result(userService.updateUser(userID, request))
            .build();
    }

    @Operation(
        summary = "Hiển thị thông tin người dùng qua token",
        description = "Người dùng xem thông tin của mình sau khi đăng nhập (tạo token)",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
            .result(userService.getMyInfo())
            .build();
    }

    @Operation(
        summary = "Xóa 1 người dùng",
        description = "",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("deleteByID/{userID}")
    ApiResponse<String> deleteUserID(@PathVariable String userID) {
        userService.deleteUserID(userID);
        return ApiResponse.<String>builder()
            .result("User has been deleted")
            .build();
    }
}
