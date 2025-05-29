package com.option1.restaurant_service.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.option1.restaurant_service.dto.request.UserCreationRequest;
import com.option1.restaurant_service.dto.request.UserUpdateRequest;
import com.option1.restaurant_service.dto.response.UserResponse;
import com.option1.restaurant_service.service.AuthenticationService;
import com.option1.restaurant_service.service.UserService;

import java.time.LocalDate;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
@Slf4j
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    private AuthenticationService authenticationService;

    private UserCreationRequest request;
    private UserResponse userResponse;
    private LocalDate dob;

    @BeforeEach
    void initData() {
        dob = LocalDate.of(2000, 1, 1);
        request = UserCreationRequest.builder()
            .username("usertest1")
            .fullname("user test")
            .password("123456789")
            .dob(dob)
            .sex("Nam")
            .phone("0379769607")
            .email("huynhchi0904@gmail.com")
            .build();

        userResponse = UserResponse.builder()
            .id("a5f2b5fdefe5")
            .username("usertest1")
            .fullname("user test")
//            .password("123456789")
            .dob(dob)
            .sex("Nam")
            .phone("0379769607")
            .email("huynhchi0904@gmail.com")
            .build();
    }


    @Test
    void createUser_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(userResponse);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
            .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("a5f2b5fdefe5"));
    }

    @Test
    void createUser_usernameInvalid_fail() throws Exception {
        log.info("UserControllerTest: createUser_usernameInvalid_fail");
        //GIVEN
        request.setUsername("hu");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

//    Mockito.when(userService.createUser(ArgumentMatchers.any()))
//        .thenReturn(userResponse);

        //WHEN , THEN
        mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("code")
                .value(1002))
            .andExpect(MockMvcResultMatchers.jsonPath("message")
                .value("Username invalid (Tên sử dụng phải có tối thiểu 3 kí tự)")
            );
    }

    @Test
    @WithMockUser(roles = "ADMIN")// Xác thực đã bị vô hiệu hóa
    void getUsers_success() throws Exception {
        // GIVEN
        List<UserResponse> users = List.of(userResponse);
        Mockito.when(userService.getUsers()).thenReturn(users);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("result[0].id").value("a5f2b5fdefe5"))
            .andExpect(MockMvcResultMatchers.jsonPath("result[0].username").value("usertest1"));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserById_success() throws Exception {
        // GIVEN
        String userID = "a5f2b5fdefe5";
        Mockito.when(userService.getUserID(userID)).thenReturn(userResponse);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/users/getByID/{userID}", userID)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("result.id").value(userID))
            .andExpect(MockMvcResultMatchers.jsonPath("result.username").value("usertest1"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUser_success() throws Exception {
        // GIVEN
        String userID = "a5f2b5fdefe5";
        UserUpdateRequest updateRequest = UserUpdateRequest.builder()
            .fullname("Updated User")
            .phone("0123456789")
            .email("updatedemail@gmail.com")
            .build();

        UserResponse updatedUserResponse = UserResponse.builder()
            .id(userID)
            .username("usertest1")
            .fullname("Updated User")
            .dob(dob)
            .sex("Nam")
            .phone("0123456789")
            .email("updatedemail@gmail.com")
            .build();

        Mockito.when(userService.updateUser(Mockito.eq(userID), Mockito.any(UserUpdateRequest.class)))
            .thenReturn(updatedUserResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(updateRequest);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.put("/users/{userID}", userID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("result.id").value(userID))
            .andExpect(MockMvcResultMatchers.jsonPath("result.fullname").value("Updated User"))
            .andExpect(MockMvcResultMatchers.jsonPath("result.phone").value("0123456789"));
    }

    @Test
    @WithMockUser(username = "usertest1", roles = "USER")
    void getMyInfo_success() throws Exception {
        // GIVEN
        Mockito.when(userService.getMyInfo()).thenReturn(userResponse);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/users/myInfo")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("a5f2b5fdefe5"))
            .andExpect(MockMvcResultMatchers.jsonPath("result.username").value("usertest1"))
            .andExpect(MockMvcResultMatchers.jsonPath("result.fullname").value("user test"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUserID_success() throws Exception {
        // GIVEN
        String userID = "a5f2b5fdefe5";

        // No need to mock `userService.deleteUserID` since it doesn't return a value.

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/deleteByID/{userID}", userID)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("result").value("User has been deleted"));
    }

}
