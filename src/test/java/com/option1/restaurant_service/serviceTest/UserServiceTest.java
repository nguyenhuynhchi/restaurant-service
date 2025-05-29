package com.option1.restaurant_service.serviceTest;


import com.option1.restaurant_service.dto.request.UserCreationRequest;
import com.option1.restaurant_service.dto.request.UserUpdateRequest;
import com.option1.restaurant_service.dto.response.UserResponse;
import com.option1.restaurant_service.entity.User;
import com.option1.restaurant_service.exception.AppException;
import com.option1.restaurant_service.exception.ErrorCode;
import com.option1.restaurant_service.mapper.UserMapper;
import com.option1.restaurant_service.repository.UserRepository;
import com.option1.restaurant_service.service.AuthenticationService;
import com.option1.restaurant_service.service.UserService;
import java.time.LocalDate;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {

    @Autowired
    private UserService userService;


    @MockBean
    private UserRepository userRepository;


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

//  @Test
//  void createUser_validRequest_success() {
//    // GIVEN
//    when(userRepository.existsByUsername(anyString())).thenReturn(false);
//    when(userRepository.save(any())).thenReturn(user);
//
//    // WHEN
//    var response = userService.createUser((request));
//
//    // THEN
//    Assertions.assertThat(response.getId()).isEqualTo("a5f2b5fdefe5");
//    Assertions.assertThat(response.getUsername()).isEqualTo("usertest1");
//  }

//  @Test
//  void createUser_validRequest_success() {
//    // GIVEN
//    when(userRepository.existsByUsername(anyString())).thenReturn(false);
//    when(userRepository.save(any())).thenReturn(user);
//
//    // WHEN
//    var response = userService.createUser(request);
//    // THEN
//
//    Assertions.assertThat(response.getId()).isEqualTo("a5f2b5fdefe5");
//    Assertions.assertThat(response.getUsername()).isEqualTo("usertest1");
//  }

//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void getUsers_adminRole_success() {
//        // GIVEN
//        List<User> users = List.of(user); // Danh sách thực thể User từ repository
//        List<UserResponse> userResponses = List.of(userResponse); // Danh sách UserResponse trả về từ mapper
//
//        // Mock repository để trả về danh sách người dùng
//        when(userRepository.findAll()).thenReturn(users);
//
//        // Mock mapper để chuyển đổi User -> UserResponse
//        when(userMapper.toUserResponse(any(User.class))).thenReturn(userResponse);
//
//        // WHEN
//        var result = userService.getUsers();
//
//        // THEN
//        Assertions.assertThat(result).hasSize(1);
//        Assertions.assertThat(result.get(0).getId()).isEqualTo("a5f2b5fdefe5");
//        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("usertest1");
//        Assertions.assertThat(result.get(0).getFullname()).isEqualTo("user test");
//    }


    @Test
    void createUser_userExisted_fail() {
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.createUser(request));
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1001);
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

        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1004);
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

        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1004);
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

        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1004);

    }

    @Test
    void deleteUserID_userExists_success() {
        // Giả lập repository tìm thấy người dùng
        when(userRepository.findById("user123")).thenReturn(Optional.of(user));

        // Thực thi phương thức
        userService.deleteUserID("user123");
    }

//    @Test
//    void createUser_validRequest_success() {   // Tạo người dùng thành công
//        // GIVEN
//        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
//        when(userMapper.toUser(request)).thenReturn(user);
//        when(userRepository.save(user)).thenReturn(user);
//        when(userMapper.toUserResponse(user)).thenReturn(userResponse);
//
//        // WHEN
//        var response = userService.createUser(request);
//
//        // THEN
//        Assertions.assertThat(response).isNotNull();
//        Assertions.assertThat(response.getUsername()).isEqualTo("usertest1");
//        Assertions.assertThat(response.getEmail()).isEqualTo("huynhchi0904@gmail.com");
//        Assertions.assertThat(response.getFullname()).isEqualTo("user test");
//
//        verify(userRepository, times(1)).existsByUsername(request.getUsername());
//        verify(userMapper, times(1)).toUser(request);
//        verify(userRepository, times(1)).save(user);
//    }

//    @Test
//    void createUser_invalidPassword_fail() {
//        // GIVEN
//        request.setPassword("123"); // Mật khẩu rỗng (không hợp lệ)
//        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
//
//        // WHEN
//        var exception = assertThrows(AppException.class, () -> userService.createUser(request));
//
//        // THEN
//        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1002); // Mã lỗi mật khẩu không hợp lệ
//
//        verify(userRepository, times(1)).existsByUsername(request.getUsername());
//        verify(userMapper, never()).toUser(request);
//        verify(passwordEncoder, never()).encode(anyString());
//    }

//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void getUsers_validRole_success() {
//        // GIVEN
//        List<User> users = List.of(user); // `user` đã được khởi tạo trong phương thức `initData`.
//        List<UserResponse> userResponses = List.of(userResponse); // `userResponse` cũng đã được khởi tạo.
//
//        when(userRepository.findAll()).thenReturn(users);
//        when(userMapper.toUserResponse(user)).thenReturn(userResponse);
//
//        // WHEN
//        List<UserResponse> result = userService.getUsers();
//
//        // THEN
//        Assertions.assertThat(result).isNotEmpty();
//        Assertions.assertThat(result).hasSize(1);
//        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("usertest1");
//
//        verify(userRepository, times(1)).findAll();
//        verify(userMapper, times(1)).toUserResponse(user);
//    }

//    @Test
//    @WithMockUser(roles = "USER") // Giả lập người dùng có vai trò không hợp lệ
//    void getUsers_invalidRole_fail() {
//        // WHEN
//        var exception = assertThrows(AuthorizationDeniedException.class, () -> userService.getUsers());
//
//        // THEN
//        Assertions.assertThat(exception.getMessage()).contains("Access Denied");
//
//        // Xác minh rằng repository không được gọi
//        verify(userRepository, never()).findAll();
//        verify(userMapper, never()).toUserResponse(any());
//    }


}
