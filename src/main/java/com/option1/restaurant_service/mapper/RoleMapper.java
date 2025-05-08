package com.option1.restaurant_service.mapper;

import com.option1.restaurant_service.dto.request.RoleRequest;
import com.option1.restaurant_service.dto.response.RoleResponse;
import com.option1.restaurant_service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
  @Mapping(target = "permissions", ignore = true)
  Role toRole(RoleRequest request);

  RoleResponse toRoleResponse(Role role);
}
