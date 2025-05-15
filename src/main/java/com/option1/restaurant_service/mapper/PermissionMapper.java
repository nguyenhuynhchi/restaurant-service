package com.option1.restaurant_service.mapper;

import com.option1.restaurant_service.dto.request.PermissionRequest;
import com.option1.restaurant_service.dto.response.PermissionResponse;
import com.option1.restaurant_service.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

  Permission toPermission(PermissionRequest request);

  PermissionResponse toPermissionResponse(Permission permission);

}
