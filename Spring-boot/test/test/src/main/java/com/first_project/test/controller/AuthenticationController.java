package com.first_project.test.controller;

import com.first_project.test.dto.request.ApiResponse;
import com.first_project.test.dto.request.AuthenticationRequest;
import com.first_project.test.dto.response.AuthenticationResponse;
import com.first_project.test.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
  AuthenticationService authenticationService;

  @PostMapping("/log-in")
  ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
    boolean result = authenticationService.authenticate(request);

    return ApiResponse.<AuthenticationResponse>builder()
        .result(AuthenticationResponse.builder().authenticated(result).build()).build();
  }
}
