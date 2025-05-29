package com.option1.restaurant_service.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.option1.restaurant_service.dto.request.PermissionRequest;
import com.option1.restaurant_service.dto.response.PermissionResponse;
import com.option1.restaurant_service.service.PermissionService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class PermissionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PermissionService permissionService;

    private PermissionRequest permissionRequest;
    private PermissionResponse permissionResponse;

    @BeforeEach
    void init() {
        permissionRequest = PermissionRequest.builder()
            .name("perm1")
            .description("Permission 1 Description")
            .build();

        permissionResponse = PermissionResponse.builder()
            .name("perm1")
            .description("Permission 1 Description")
            .build();
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void createPermission_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(permissionRequest);
        when(permissionService.create(ArgumentMatchers.any())).thenReturn(permissionResponse);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/permissions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
            .andExpect(MockMvcResultMatchers.jsonPath("result.name").value("perm1"))
            .andExpect(MockMvcResultMatchers.jsonPath("result.description")
                .value("Permission 1 Description"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllPermissions_success() throws Exception {
        // GIVEN
        List<PermissionResponse> permissions = List.of(permissionResponse);
        when(permissionService.getAll()).thenReturn(permissions);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/permissions")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("result[0].name").value("perm1"))
            .andExpect(MockMvcResultMatchers.jsonPath("result[0].description")
                .value("Permission 1 Description"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deletePermission_success() throws Exception {
        // GIVEN
        String permissionName = "perm1";
        doNothing().when(permissionService).delete(permissionName);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/permissions/{permission}", permissionName)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
            .andExpect(MockMvcResultMatchers.jsonPath("result").doesNotExist());
    }
}
