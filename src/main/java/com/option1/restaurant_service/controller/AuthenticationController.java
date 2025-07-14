package com.option1.restaurant_service.controller;

import com.nimbusds.jose.JOSEException;
import com.option1.restaurant_service.dto.response.ApiResponse;
import com.option1.restaurant_service.dto.request.AuthenticationRequest;
import com.option1.restaurant_service.dto.request.IntrospectRequest;
import com.option1.restaurant_service.dto.request.LogoutRequest;
import com.option1.restaurant_service.dto.request.RefreshRequest;
import com.option1.restaurant_service.dto.response.AuthenticationResponse;
import com.option1.restaurant_service.dto.response.IntrospectResponse;
import com.option1.restaurant_service.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Authentication API", description = "API xác thực người dùng")
public class AuthenticationController {
    AuthenticationService authenticationService;

    @Operation(
        summary = "Đăng nhập lấy token",
        description = "Khi người dùng đăng nhập sẽ trả về token"
    )
    @PostMapping("/token")
//    @CrossOrigin(origins = "http://localhost:3000") // Allow frontend domain
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);

        return ApiResponse.<AuthenticationResponse>builder()
            .result(result).build();
    }


    @Operation(
        summary = "Kiểm tra token",
        description = "Kiểm tra token đó còn hiệu lực không"
    )
    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
        throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
            .result(result)
            .build();
    }

    @Operation(
        summary = "Làm mới token",
        description = "Làm mới token đã hết hiệu lực"
    )
    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody RefreshRequest request)
        throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);

        return ApiResponse.<AuthenticationResponse>builder()
            .result(result).build();
    }

    @Operation(
        summary = "Đăng xuất",
        description = "Đăng xuất và vô hiệu hóa token",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/logout")
    ApiResponse<String> logout(@RequestBody LogoutRequest request)
        throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<String>builder().result("You have logged out").build();
    }
}

