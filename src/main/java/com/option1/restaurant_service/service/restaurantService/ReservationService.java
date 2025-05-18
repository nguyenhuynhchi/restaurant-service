package com.option1.restaurant_service.service.restaurantService;

import com.option1.restaurant_service.dto.request.ReservationRequest;
import com.option1.restaurant_service.dto.response.ReservationResponse;
import com.option1.restaurant_service.entity.Reservation;
import com.option1.restaurant_service.entity.Restaurant;
import com.option1.restaurant_service.entity.User;
import com.option1.restaurant_service.exception.AppException;
import com.option1.restaurant_service.exception.ErrorCode;
import com.option1.restaurant_service.mapper.ReservationMapper;
import com.option1.restaurant_service.repository.ReservationRepository;
import com.option1.restaurant_service.repository.RestaurantRepository;
import com.option1.restaurant_service.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReservationService {
    ReservationRepository reservationRepository;
    ReservationMapper reservationMapper;

    UserRepository userRepository;
    RestaurantRepository restaurantRepository;

    public ReservationResponse createReservation(ReservationRequest request){
        Reservation reservation = reservationMapper.toReservation(request);

        // Lấy id user. Giống với get info lấy thông tin có chứa tên từ token của user
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(
            () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurant()).orElseThrow(
            () -> new AppException(ErrorCode.ID_RESTAURANT_NOT_EXISTED));


        reservation.setRequestTime(LocalDateTime.now());
        reservation.setUser(user);
        reservation.setRestaurant(restaurant);


        reservation = reservationRepository.save(reservation);
        return reservationMapper.toReservationResponse(reservation);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<ReservationResponse> getAllReservation(){
        var reservation = reservationRepository.findAll();
        return reservation.stream().map(reservationMapper::toReservationResponse).toList();
    }
}
