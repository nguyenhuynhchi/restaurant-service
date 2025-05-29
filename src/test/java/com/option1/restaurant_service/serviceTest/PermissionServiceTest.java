package com.option1.restaurant_service.serviceTest;

import com.option1.restaurant_service.dto.request.PermissionRequest;
import com.option1.restaurant_service.dto.response.PermissionResponse;
import com.option1.restaurant_service.entity.Permission;
import com.option1.restaurant_service.mapper.PermissionMapper;
import com.option1.restaurant_service.repository.PermissionRepository;
import com.option1.restaurant_service.service.PermissionService;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource("/test.properties")
public class PermissionServiceTest {
    @MockBean
    private PermissionRepository permissionRepository;

    @MockBean
    private PermissionMapper permissionMapper;

    @Autowired
    private PermissionService permissionService;

    private Permission permission;
    private PermissionRequest permissionRequest;
    private PermissionResponse permissionResponse;

    @BeforeEach
    void init() {
        permission = Permission.builder()
            .name("perm1")
            .description("Permission 1")
            .build();

        permissionRequest = PermissionRequest.builder()
            .name("perm1")
            .description("Permission 1")
            .build();

        permissionResponse = PermissionResponse.builder()
            .name("perm1")
            .description("Permission 1")
            .build();
    }

    @Test
    void createPermission_success() {
        // GIVEN
        when(permissionMapper.toPermission(permissionRequest)).thenReturn(permission);
        when(permissionRepository.save(permission)).thenReturn(permission);
        when(permissionMapper.toPermissionResponse(permission)).thenReturn(permissionResponse);

        // WHEN
        PermissionResponse result = permissionService.create(permissionRequest);

        // THEN
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getName()).isEqualTo("perm1");
        Assertions.assertThat(result.getDescription()).isEqualTo("Permission 1");
    }


    @Test
    void getAllPermissions_success() {
        // GIVEN
        List<Permission> permissions = List.of(permission);
        when(permissionRepository.findAll()).thenReturn(permissions);
        when(permissionMapper.toPermissionResponse(permission)).thenReturn(permissionResponse);

        // WHEN
        List<PermissionResponse> result = permissionService.getAll();

        // THEN
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).hasSize(1);
        Assertions.assertThat(result.get(0).getName()).isEqualTo("perm1");
        Assertions.assertThat(result.get(0).getDescription()).isEqualTo("Permission 1");

    }

    @Test
    void deletePermission_success() {
        // Mock repository
        doNothing().when(permissionRepository).deleteById(anyString());

        permissionService.delete("perm1");
    }
}
