package com.option1.restaurant_service.mapper;

import com.option1.restaurant_service.dto.request.ReservationRequest;
import com.option1.restaurant_service.dto.response.ReservationResponse;
import com.option1.restaurant_service.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "restaurant", ignore = true)
    Reservation toReservation(ReservationRequest request);

    //
    @Mapping(target = "restaurant",
        expression = "java(reservation.getRestaurant() != null ? reservation.getRestaurant().getId() : null)")
    @Mapping(target = "user",
        expression = "java(reservation.getUser() != null ? reservation.getUser().getFullname() : null)")
    @Mapping(target = "table",
        expression = "java(reservation.getTable() != null ? reservation.getTable().getId() : null)")
//    @Mapping(target = "messenger",
//        expression = "java(reservation.getMessenger() != null ? reservation.getMessenger() : null)")
    ReservationResponse toReservationResponse(Reservation reservation);
}
