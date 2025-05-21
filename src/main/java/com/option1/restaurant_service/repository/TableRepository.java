package com.option1.restaurant_service.repository;

import com.option1.restaurant_service.entity.DiningTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends JpaRepository<DiningTable, String> {

}
