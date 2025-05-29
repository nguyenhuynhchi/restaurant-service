package com.option1.restaurant_service.service;

import com.option1.restaurant_service.dto.request.RoleRequest;
import com.option1.restaurant_service.dto.response.RoleResponse;
import com.option1.restaurant_service.entity.Role;
import com.option1.restaurant_service.mapper.RoleMapper;
import com.option1.restaurant_service.repository.PermissionRepository;
import com.option1.restaurant_service.repository.RoleRepository;
import java.util.HashSet;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
  RoleRepository roleRepository;
  PermissionRepository permissionRepository;
  RoleMapper roleMapper;


  public RoleResponse create(RoleRequest request){
    var role = roleMapper.toRole(request);

    var permission = permissionRepository.findAllById((request.getPermissions()));
    role.setPermissions(new HashSet<>(permission));

    role = roleRepository.save(role);
    return roleMapper.toRoleResponse(role);
  }

//  @PreAuthorize("hasRole('ADMIN')")
  public List<RoleResponse> getAll(){
    return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
  }

  public void delete(String role){
    roleRepository.deleteById(role);
  }
}
