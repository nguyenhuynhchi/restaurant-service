package com.option1.restaurant_service.service.restaurantService;

import com.option1.restaurant_service.dto.request.ConfirmReservationRequest;
import com.option1.restaurant_service.dto.request.ReservationFilterRequest;
import com.option1.restaurant_service.dto.request.ReservationRequest;
import com.option1.restaurant_service.dto.response.ReservationResponse;
import com.option1.restaurant_service.entity.DiningTable;
import com.option1.restaurant_service.entity.Reservation;
import com.option1.restaurant_service.entity.Restaurant;
import com.option1.restaurant_service.entity.User;
import com.option1.restaurant_service.enums.StatusReservation;
import com.option1.restaurant_service.exception.AppException;
import com.option1.restaurant_service.exception.ErrorCode;
import com.option1.restaurant_service.mapper.ReservationMapper;
import com.option1.restaurant_service.repository.ReservationRepository;
import com.option1.restaurant_service.repository.RestaurantRepository;
import com.option1.restaurant_service.repository.TableRepository;
import com.option1.restaurant_service.repository.UserRepository;
import java.time.Duration;
import java.time.LocalDateTime;
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
    TableRepository tableRepository;
    RestaurantRepository restaurantRepository;


    public ReservationResponse createReservation(ReservationRequest request) {
        Reservation reservation = reservationMapper.toReservation(request);

        // Lấy id user. Giống với get info lấy thông tin có chứa tên từ token của user
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(
            () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurant()).orElseThrow(
            () -> new AppException(ErrorCode.ID_RESTAURANT_NOT_EXISTED));

        Duration duration = Duration.between(LocalDateTime.now(), reservation.getReservationTime());
        long hoursDifference = duration.toHours();

        int durationReservation = 5; // thời gian đặt bàn phải trước 5 tiếng

        if (hoursDifference < durationReservation) {
            throw new AppException(ErrorCode.OUTTIME_RESERVATION);
        } else {
            reservation.setRequestTime(LocalDateTime.now());
            reservation.setUser(user);
            reservation.setStatus(StatusReservation.UNCONFIRMED.getDescription());
            reservation.setRestaurant(restaurant);
        }
        return reservationMapper.toReservationResponse(reservationRepository.save(reservation));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ReservationResponse confirmReservation(String reservationID,
        ConfirmReservationRequest request) {   // Confirm đặt bàn và cập nhật mã bàn cho đơn đặt
        Reservation reservation = reservationRepository.findById(reservationID)
            .orElseThrow(() -> new AppException(ErrorCode.ID_RESERVATION_NOT_EXISTED));

        DiningTable table = tableRepository.findById(request.getTable())
            .orElseThrow(() -> new AppException(ErrorCode.ID_TABLE_NOT_EXISTED));

        reservation.setTable(table);

        reservation.setStatus(StatusReservation.CONFIRMED.getDescription());

        return reservationMapper.toReservationResponse(reservationRepository.save(reservation));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<ReservationResponse> getReservationUnconfirmed(){

        List<Reservation> reservationUnconfirmed = reservationRepository.findAll()
            .stream()
            .filter(reservation -> reservation.getStatus().equals(StatusReservation.UNCONFIRMED.getDescription()))
            .filter(reservation -> reservation.getReservationTime().isAfter(LocalDateTime.now()))
            .toList();

        return reservationUnconfirmed.stream().map(reservationMapper::toReservationResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<ReservationResponse> getReservationFilter(ReservationFilterRequest request){
        List<Reservation> reservationUnconfirmed = reservationRepository.findAll()
            .stream()
//            .filter(reservation -> reservation.getStatus().equals(StatusReservation.CONFIRMED.getDescription()))
            .filter(reservation -> reservation.getReservationTime().isAfter(LocalDateTime.now()))
            .filter(reservation -> reservation.getRestaurant().getId().equals(request.getRestaurant()))
            .filter(reservation -> reservation.getTable() != null
                && reservation.getTable().getCapacity() >= request.getQuantityPeople()
                && reservation.getTable().getCapacity() <= request.getQuantityPeople() + 1)
            .toList();

        return reservationUnconfirmed.stream().map(reservationMapper::toReservationResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<ReservationResponse> getAllReservation() {
        var reservation = reservationRepository.findAll();
        return reservation.stream().map(reservationMapper::toReservationResponse).toList();
    }

    public List<ReservationResponse> getReservationComing(){
        // Lấy user từ token.
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(
            () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<Reservation> comingReservations  = reservationRepository.findAll()
            .stream()
            .filter(reservation -> reservation.getUser().getId().equals(user.getId()))
            .filter(reservation -> reservation.getReservationTime().isAfter(LocalDateTime.now()))
            .toList();

        return comingReservations.stream()
            .map(reservationMapper::toReservationResponse)
            .toList();
    }

    public List<ReservationResponse> getReservationByUserId() {   //Xem lịch sử đặt bàn của khách
        // Lấy user từ token.
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(
            () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Lấy tất cả reservation và lọc theo userId.
        List<Reservation> reservations = reservationRepository.findAll()
            .stream()
            .filter(reservation -> reservation.getUser().getId().equals(user.getId()))
            .toList();

        return reservations.stream().map(reservationMapper::toReservationResponse).toList();
    }

    public String cancleReservation(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new AppException(ErrorCode.ID_RESERVATION_NOT_EXISTED));

        Duration duration = Duration.between(LocalDateTime.now(), reservation.getReservationTime());
        long hoursDifference = duration.toHours();

        String statusCancle = null;
        int durationCancle = 1; // chỉ được hủy bàn trước 1 tiếng, sau 1 tiếng thì phải liên hệ quản lý

        if (hoursDifference >= durationCancle) {
            reservation.setStatus(StatusReservation.TABLE_CANCLE.getDescription());
            reservationRepository.save(reservation);
            statusCancle = reservation.getStatus();
        } else {
            throw new AppException(ErrorCode.OUTTIME_CANCLE);
        }
        return statusCancle;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public String adminCancleReservation(String reservationID) {
        Reservation reservation = reservationRepository.findById(reservationID)
            .orElseThrow(() -> new AppException(ErrorCode.ID_RESERVATION_NOT_EXISTED));

        reservation.setStatus(StatusReservation.TABLE_CANCLE_ADMIN.getDescription());
        reservationRepository.save(reservation);

        return reservation.getStatus();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ReservationResponse gotTable(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new AppException(ErrorCode.ID_RESERVATION_NOT_EXISTED));

        if (reservation.getTable() == null) {
            throw new AppException(ErrorCode.NOT_HAVE_TABLE);
        } else {
            reservation.setStatus(StatusReservation.GOT_TABLE.getDescription());
        }

        return reservationMapper.toReservationResponse(reservationRepository.save(reservation));
    }
}
