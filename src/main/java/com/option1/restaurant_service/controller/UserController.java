package com.option1.restaurant_service.controller;

import com.option1.restaurant_service.dto.request.ApiResponse;
import com.option1.restaurant_service.dto.request.UserCreationRequest;
import com.option1.restaurant_service.dto.response.UserResponse;
import com.option1.restaurant_service.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

  UserService userService;

  @PostMapping
  ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
    return ApiResponse.<UserResponse>builder()
        .result(userService.createUser(request)).build();
  }

  @GetMapping
  ApiResponse<List<UserResponse>> getUsers(){
    return ApiResponse.<List<UserResponse>>builder()
        .result(userService.getUsers())
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
