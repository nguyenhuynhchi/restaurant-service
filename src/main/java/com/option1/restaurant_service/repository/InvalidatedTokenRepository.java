package com.option1.restaurant_service.repository;

import com.option1.restaurant_service.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {

}
