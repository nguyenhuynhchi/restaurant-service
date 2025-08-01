package com.option1.restaurant_service.controller;

import com.option1.restaurant_service.dto.response.ApiResponse;
import com.option1.restaurant_service.dto.request.RoleRequest;
import com.option1.restaurant_service.dto.response.RoleResponse;
import com.option1.restaurant_service.service.RoleService;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
// @PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
  RoleService roleService;

  @PostMapping
  ApiResponse<RoleResponse> create(@RequestBody RoleRequest request){
    return ApiResponse.<RoleResponse>builder()
        .result(roleService.create(request))
        .build();
  }


  @GetMapping
  ApiResponse<List<RoleResponse>> getAll(){
    return ApiResponse.<List<RoleResponse>>builder()
        .result(roleService.getAll())
        .build();
  }

  @DeleteMapping("/{role}")
  ApiResponse<Void> delete(@PathVariable String role){
    roleService.delete(role);
    return ApiResponse.<Void>builder().build();
  }
}
