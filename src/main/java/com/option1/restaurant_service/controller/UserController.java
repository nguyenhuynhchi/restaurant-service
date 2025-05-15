package com.option1.restaurant_service.controller;

import com.option1.restaurant_service.dto.request.ApiResponse;
import com.option1.restaurant_service.dto.request.UserCreationRequest;
import com.option1.restaurant_service.dto.request.UserUpdateRequest;
import com.option1.restaurant_service.dto.response.UserResponse;
import com.option1.restaurant_service.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
@Slf4j
public class UserController {

    UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
            .result(userService.createUser(request)).build();
    }

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

    @GetMapping("/getByID/{userID}")
    ApiResponse<UserResponse> getUserID(@PathVariable("userID") String userID) {
        return ApiResponse.<UserResponse>builder()
            .result(userService.getUserID(userID))
            .build();
    }

    @PutMapping("/{userID}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userID,
        @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
            .result(userService.updateUser(userID, request))
            .build();
    }

    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
            .result(userService.getMyInfo())
            .build();
    }

    @DeleteMapping("deleteByID/{userID}")
    ApiResponse<String> deleteUserID(@PathVariable String userID) {
        userService.deleteUserID(userID);
        return ApiResponse.<String>builder()
            .result("User has been deleted")
            .build();
    }
}
