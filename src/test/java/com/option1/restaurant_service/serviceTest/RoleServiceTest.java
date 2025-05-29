package com.option1.restaurant_service.serviceTest;

import com.option1.restaurant_service.dto.request.RoleRequest;
import com.option1.restaurant_service.dto.response.PermissionResponse;
import com.option1.restaurant_service.dto.response.RoleResponse;
import com.option1.restaurant_service.entity.Permission;
import com.option1.restaurant_service.entity.Role;
import com.option1.restaurant_service.mapper.RoleMapper;
import com.option1.restaurant_service.repository.PermissionRepository;
import com.option1.restaurant_service.repository.RoleRepository;
import com.option1.restaurant_service.service.RoleService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.assertj.core.api.Assertions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource("/test.properties")
public class RoleServiceTest {

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private PermissionRepository permissionRepository;

    @MockBean
    private RoleMapper roleMapper;

    @Autowired
    private RoleService roleService;

    private RoleRequest roleRequest;
    private Role role;
    private RoleResponse roleResponse;
    private Permission permission;
    private PermissionResponse permissionResponse;

    @BeforeEach
    void init() {
        permission = Permission.builder()
            .name("perm1")
            .description("Permission 1")
            .build();

        permissionResponse = PermissionResponse.builder()
            .name("perm1")
            .description("Permission 1")
            .build();

        roleRequest = RoleRequest.builder()
            .name("Admin")
            .permissions(Set.of("perm1"))
            .build();


        role = Role.builder()
            .name("Admin")
            .description("Admin")
            .permissions(new HashSet<>(List.of(permission)))
            .build();

        roleResponse = RoleResponse.builder()
            .name("Admin")
            .description("Admin")
            .permissions(Set.of(permissionResponse))
            .build();

    }

    @Test
    void createRole_success() {
        // Mock mapper
        when(roleMapper.toRole(any(RoleRequest.class))).thenReturn(role);

        // Mock repository
        when(permissionRepository.findAllById(anyList())).thenReturn(List.of(permission));
        when(roleRepository.save(any(Role.class))).thenReturn(role);
        when(roleMapper.toRoleResponse(any(Role.class))).thenReturn(roleResponse);

        RoleResponse result = roleService.create(roleRequest);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getName()).isEqualTo("Admin");
        Assertions.assertThat(result.getDescription()).isEqualTo("Admin");
        Assertions.assertThat(result.getPermissions()).isEqualTo(Set.of(permissionResponse));
    }

    @Test
    void getAllRoles_success() {
        // Mock repository
        when(roleRepository.findAll()).thenReturn(List.of(role));
        when(roleMapper.toRoleResponse(any(Role.class))).thenReturn(roleResponse);

        List<RoleResponse> result = roleService.getAll();

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).hasSize(1);
        Assertions.assertThat(result.get(0).getName()).isEqualTo("Admin");
        Assertions.assertThat(result.get(0).getDescription()).isEqualTo("Admin");
    }

    @Test
    void deleteRole_success() {
        // Mock repository
        doNothing().when(roleRepository).deleteById(anyString());

        roleService.delete("Admin");

    }
}
