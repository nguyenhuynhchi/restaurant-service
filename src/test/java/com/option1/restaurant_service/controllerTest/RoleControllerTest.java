package com.option1.restaurant_service.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.option1.restaurant_service.dto.request.RoleRequest;
import com.option1.restaurant_service.dto.response.PermissionResponse;
import com.option1.restaurant_service.dto.response.RoleResponse;
import com.option1.restaurant_service.dto.response.UserResponse;
import com.option1.restaurant_service.service.RoleService;
import java.util.List;
import java.util.Set;
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

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")

public class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

    private RoleRequest roleRequest;
    private RoleResponse roleResponse;
    PermissionResponse perm1;
    PermissionResponse perm2;

    @BeforeEach
    void init() {
        roleRequest = RoleRequest.builder()
            .name("Admin")
            .description("Administrator Role")
            .permissions(Set.of("perm1", "perm2"))
            .build();

        perm1 = PermissionResponse.builder()
            .name("perm1")
            .description("Permission 1")
            .build();

        perm2 = PermissionResponse.builder()
            .name("perm2")
            .description("Permission 2")
            .build();

        roleResponse = RoleResponse.builder()
            .name("Admin")
            .description("Administrator Role")
            .permissions(Set.of(perm1, perm2))
            .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createRole_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(roleRequest);
        when(roleService.create(ArgumentMatchers.any())).thenReturn(roleResponse);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/roles")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
            .andExpect(MockMvcResultMatchers.jsonPath("result.name").value("Admin"))
            .andExpect(
                MockMvcResultMatchers.jsonPath("result.description").value("Administrator Role"))
            .andExpect(MockMvcResultMatchers.jsonPath("result.permissions[*].name")
                .value(containsInAnyOrder("perm1", "perm2")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllRole_success() throws Exception {
        List<RoleResponse> roles = List.of(roleResponse);
        Mockito.when(roleService.getAll()).thenReturn(roles);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/roles")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("result[0].name").value("Admin"))
            .andExpect(
                MockMvcResultMatchers.jsonPath("result[0].description").value("Administrator Role"))
            .andExpect(MockMvcResultMatchers.jsonPath("result[0].permissions[*].name")
                .value(containsInAnyOrder("perm1", "perm2")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteRole_success() throws Exception {
        // GIVEN
        String roleName = "Admin";

        // Mock behavior của service method
        doNothing().when(roleService).delete(roleName);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/roles/{role}", roleName)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
            .andExpect(MockMvcResultMatchers.jsonPath("result").doesNotExist());
    }

}
