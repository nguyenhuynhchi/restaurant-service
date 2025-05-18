package com.option1.restaurant_service.repository;

import com.option1.restaurant_service.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, String> {

}
