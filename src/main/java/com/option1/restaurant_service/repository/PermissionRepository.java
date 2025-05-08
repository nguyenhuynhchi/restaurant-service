package com.option1.restaurant_service.repository;

import com.option1.restaurant_service.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, String> {

}
