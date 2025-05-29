package com.option1.restaurant_service.serviceTest;

import com.option1.restaurant_service.dto.request.AuthenticationRequest;
import com.option1.restaurant_service.dto.response.AuthenticationResponse;
import com.option1.restaurant_service.entity.User;
import com.option1.restaurant_service.exception.AppException;
import com.option1.restaurant_service.exception.ErrorCode;
import com.option1.restaurant_service.repository.InvalidatedTokenRepository;
import com.option1.restaurant_service.repository.UserRepository;
import com.option1.restaurant_service.service.AuthenticationService;
import java.util.Optional;
import java.util.Set;
import lombok.experimental.NonFinal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
@TestPropertySource("/test.properties")
public class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private InvalidatedTokenRepository invalidatedTokenRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authenticationService, "SIGNER_KEY", SIGNER_KEY);
        ReflectionTestUtils.setField(authenticationService, "VALID_DURATION", VALID_DURATION); // 1 giờ
        ReflectionTestUtils.setField(authenticationService, "REFRESHABLE_DURATION", REFRESHABLE_DURATION); // 7 ngày
    }

    @Test
    void authenticate_ValidCredentials_ReturnsAuthenticatedResponse() { // authenticate với thông tin hợp lệ
        // Arrange
        String username = "testUser";
        String password = "password123";
        String encodedPassword = new BCryptPasswordEncoder(10).encode(password);

        User mockUser = User.builder()
            .username(username)
            .password(encodedPassword)
            .roles(Set.of()) // Bỏ qua vai trò để đơn giản hóa
            .build();

        Mockito.when(userRepository.findByUsername(username))
            .thenReturn(Optional.of(mockUser));

        AuthenticationRequest request = new AuthenticationRequest(username, password);

        // Act
        AuthenticationResponse response = authenticationService.authenticate(request);

        // Assert
        Assertions.assertNotNull(response.getToken());
        Assertions.assertTrue(response.isAuthenticated());
    }


    @Test
    void authenticate_UserDoesNotExist_ThrowsException() {  // authenticate khi người dùng không tồn tại
        // Arrange
        String username = "nonExistentUser";
        AuthenticationRequest request = new AuthenticationRequest(username, "password123");

        Mockito.when(userRepository.findByUsername(username))
            .thenReturn(Optional.empty());

        // Act & Assert
        AppException exception = Assertions.assertThrows(AppException.class, () -> {
            authenticationService.authenticate(request);
        });

        Assertions.assertEquals(ErrorCode.USER_NOT_EXISTED, exception.getErrorCode());
    }

    @Test
    void authenticate_InvalidPassword_ThrowsException() {  // authenticate với mật khẩu sai
        // Arrange
        String username = "testUser";
        String password = "password123";
        String wrongPassword = "wrongPassword";

        User mockUser = User.builder()
            .username(username)
            .password(new BCryptPasswordEncoder(10).encode(password))
            .roles(Set.of()) // Bỏ qua vai trò để đơn giản hóa
            .build();

        Mockito.when(userRepository.findByUsername(username))
            .thenReturn(Optional.of(mockUser));

        AuthenticationRequest request = new AuthenticationRequest(username, wrongPassword);

        // Act & Assert
        AppException exception = Assertions.assertThrows(AppException.class, () -> {
            authenticationService.authenticate(request);
        });

        Assertions.assertEquals(ErrorCode.UNAUTHENTICATED, exception.getErrorCode());
    }
}
