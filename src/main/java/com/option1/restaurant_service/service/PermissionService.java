package com.option1.restaurant_service.service;

import com.option1.restaurant_service.dto.request.PermissionRequest;
import com.option1.restaurant_service.dto.response.PermissionResponse;
import com.option1.restaurant_service.entity.Permission;
import com.option1.restaurant_service.mapper.PermissionMapper;
import com.option1.restaurant_service.repository.PermissionRepository;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {

  PermissionRepository permissionRepository;
  PermissionMapper permissionMapper;

  public PermissionResponse create(PermissionRequest request) {
    Permission permission = permissionMapper.toPermission(request);
    permission = permissionRepository.save(permission);
    return permissionMapper.toPermissionResponse(permission);
  }

  public List<PermissionResponse> getAll(){
    var permissions = permissionRepository.findAll();
    return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
  }

  public void delete(String Permission){
    permissionRepository.deleteById(Permission);
  }
}
