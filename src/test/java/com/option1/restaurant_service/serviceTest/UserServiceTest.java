package com.option1.restaurant_service.serviceTest;


import com.option1.restaurant_service.dto.request.UserCreationRequest;
import com.option1.restaurant_service.dto.request.UserUpdateRequest;
import com.option1.restaurant_service.dto.response.UserResponse;
import com.option1.restaurant_service.entity.User;
import com.option1.restaurant_service.exception.AppException;
import com.option1.restaurant_service.exception.ErrorCode;
import com.option1.restaurant_service.mapper.UserMapper;
import com.option1.restaurant_service.repository.RoleRepository;
import com.option1.restaurant_service.repository.UserRepository;
import com.option1.restaurant_service.service.AuthenticationService;
import com.option1.restaurant_service.service.UserService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {

    @Autowired
    private UserService userService;


    @MockBean
    private UserRepository userRepository;
    private RoleRepository roleRepository;


    private UserMapper userMapper;
    private UserCreationRequest request;
    private UserResponse userResponse;
    private AuthenticationService authenticationService;
    private User user;
    private LocalDate dob;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void initData() {
        dob = LocalDate.of(1990, 1, 1);
        request = UserCreationRequest.builder()
            .username("usertest1")
            .fullname("user test")
            .password("123456789")
            .dob(dob)
            .sex("Nam")
            .phone("0123456789")
            .email("usertest@gmail.com")
            .build();

        userResponse = UserResponse.builder()
            .id("a5f2b5fdefe5")
            .username("usertest1")
            .fullname("user test")
            .dob(dob)
            .sex("Nam")
            .phone("0123456789")
            .email("usertest@gmail.com")
            .build();

        user = User.builder()
            .id("a5f2b5fdefe5")
            .username("usertest1")
            .fullname("user test")
            .dob(dob)
            .sex("Nam")
            .phone("0123456789")
            .email("usertest@gmail.com")
            .build();
    }


    @Test
    void createUser_userExisted_fail() {
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.createUser(request));
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1001);
    }

    @Test
    void updateUser_userNotFound_throwsException_() {
        // Arrange
        String userId = "non_existing_user_id";
        UserUpdateRequest request = new UserUpdateRequest();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        AppException exception = assertThrows(AppException.class, () ->
            userService.updateUser(userId, request));

        // Assert
        assertEquals(ErrorCode.USER_NOT_EXISTED, exception.getErrorCode());
//        verify(userRepository, times(1)).findById(userId);
//        verifyNoMoreInteractions(userRepository); // vì không save
    }


    @Test
    @WithMockUser(username = "usertest1")
    void getMyInfo_valid_success() {  // Người dùng hợp lệ
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        var response = userService.getMyInfo();

        Assertions.assertThat(response.getId()).isEqualTo("a5f2b5fdefe5");
        Assertions.assertThat(response.getUsername()).isEqualTo("usertest1");
        Assertions.assertThat(response.getFullname()).isEqualTo("user test");
        Assertions.assertThat(response.getEmail()).isEqualTo("usertest@gmail.com");
    }

    @Test
    @WithMockUser(username = "usertest1")
    void getMyInfo_userNotFound_err() {  // Người dùng không tồn tại
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(null));

        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.getMyInfo());

        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1008);
    }


    @Test
    void getMyInfo_noSecurityContext_error() {  // Không có thông tin SecurityContext
        // GIVEN: Không có thông tin người dùng trong SecurityContext
        SecurityContextHolder.clearContext();

        // WHEN
        var exception = assertThrows(NullPointerException.class, () -> userService.getMyInfo());

        // THEN
        Assertions.assertThat(exception.getMessage()).contains("getAuthentication");

        verify(userRepository, never()).findByUsername(anyString());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserID_userExisted_success() {  // getUserId tồn tại
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        var response = userService.getUserID(user.getId());

        Assertions.assertThat(response.getId()).isEqualTo("a5f2b5fdefe5");
        Assertions.assertThat(response.getUsername()).isEqualTo("usertest1");
        Assertions.assertThat(response.getFullname()).isEqualTo("user test");
        Assertions.assertThat(response.getEmail()).isEqualTo("usertest@gmail.com");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserID_userNotExisted_fail() {  // getUserId không tồn tại
        when(userRepository.findById(anyString())).thenReturn(Optional.ofNullable(null));

        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.getUserID("abc"));

        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1008);
    }

    @Test
    void updateUser_userNotFound_throwsException() {
        // Giả lập không tìm thấy người dùng
        when(userRepository.findById(anyString())).thenReturn(Optional.ofNullable(null));

        UserUpdateRequest updateRequest = UserUpdateRequest.builder().build();

        // Thực thi phương thức và kiểm tra ngoại lệ
        var exception = assertThrows(AppException.class,
            () -> userService.updateUser("invalidUser", updateRequest));

        Assertions.assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_EXISTED);
    }

    @Test
    void deleteUserID_userNotFound_throwsException() {
        // Giả lập repository không tìm thấy người dùng
        when(userRepository.findById(anyString())).thenReturn(Optional.ofNullable(null));

        // Thực thi phương thức và kiểm tra ngoại lệ
        var exception = assertThrows(AppException.class,
            () -> userService.deleteUserID("invalidUser"));

        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1008);

    }

    @Test
    void deleteUserID_userExists_success() {
        // Giả lập repository tìm thấy người dùng
        when(userRepository.findById("user123")).thenReturn(Optional.of(user));

        // Thực thi phương thức
        userService.deleteUserID("user123");
    }
}
