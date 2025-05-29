package com.option1.restaurant_service.serviceTest;

import com.option1.restaurant_service.dto.request.ConfirmReservationRequest;
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
import com.option1.restaurant_service.service.restaurantService.ReservationService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.Authentication;
import org.assertj.core.api.Assertions;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("/test.properties")
public class ReservationServiceTest {

    @MockBean
    private ReservationRepository reservationRepository;

    @MockBean
    private ReservationMapper reservationMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TableRepository tableRepository;

    @MockBean
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ReservationService reservationService;

    private ReservationRequest reservationRequest;
    private ConfirmReservationRequest confirmReservationRequest;
    private Reservation reservation;
    private ReservationResponse reservationResponse;
    private DiningTable diningTable;
    private Restaurant restaurant;
    private User user;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        reservationRequest = ReservationRequest.builder()
            .restaurant("R01")
            .reservationTime(LocalDateTime.now().plusHours(6))
            .build();

        confirmReservationRequest = ConfirmReservationRequest.builder()
            .table("T01")
            .build();

        user = User.builder()
            .id("U01")
            .username("user1")
            .build();

        restaurant = Restaurant.builder()
            .id("R01")
            .build();

        diningTable = DiningTable.builder()
            .id("T01")
            .build();

        reservation = Reservation.builder()
            .id("Res01")
            .reservationTime(LocalDateTime.now().plusHours(6))
            .user(user)
            .restaurant(restaurant)
            .status(StatusReservation.UNCONFIRMED.getDescription())
            .build();

        reservationResponse = ReservationResponse.builder()
            .id("Res01")
            .status(StatusReservation.UNCONFIRMED.getDescription())
            .build();
    }

    @Test
    void createReservation_success() {
        // Mock security context
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("user1");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Mock repository and mapper
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(anyString())).thenReturn(Optional.of(restaurant));
        when(reservationMapper.toReservation(any(ReservationRequest.class))).thenReturn(
            reservation);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toReservationResponse(any(Reservation.class))).thenReturn(
            reservationResponse);

        ReservationResponse result = reservationService.createReservation(reservationRequest);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo("Res01");
        Assertions.assertThat(result.getStatus())
            .isEqualTo(StatusReservation.UNCONFIRMED.getDescription());
    }

    @Test
    void createReservation_userNotFound() {
        // Mock security context
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("user1");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> reservationService.createReservation(reservationRequest))
            .isInstanceOf(AppException.class)
            .hasMessage(ErrorCode.USER_NOT_EXISTED.getMessage());
    }

    @Test
    void createReservation_restaurantNotFound() {
        // Mock security context
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("user1");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(anyString())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> reservationService.createReservation(reservationRequest))
            .isInstanceOf(AppException.class)
            .hasMessage(ErrorCode.ID_RESTAURANT_NOT_EXISTED.getMessage());
    }

    @Test
    void createReservation_outtimeReservation() {
        // Mock security context
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("user1");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(anyString())).thenReturn(Optional.of(restaurant));
        when(reservationMapper.toReservation(any(ReservationRequest.class))).thenReturn(reservation);

        // Set reservation time ít hơn 5 tiếng
        reservation.setReservationTime(LocalDateTime.now().plusHours(3)); // 3 hours from now

        Assertions.assertThatThrownBy(() -> reservationService.createReservation(reservationRequest))
            .isInstanceOf(AppException.class)
            .hasMessage(ErrorCode.OUTTIME_RESERVATION.getMessage());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void confirmReservation_success() {
        // Mock repository and mapper
        when(reservationRepository.findById(anyString())).thenReturn(Optional.of(reservation));
        when(tableRepository.findById(anyString())).thenReturn(Optional.of(diningTable));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        reservationResponse.setStatus(StatusReservation.CONFIRMED.getDescription());
        when(reservationMapper.toReservationResponse(reservation)).thenReturn(reservationResponse);

        ReservationResponse result = reservationService.confirmReservation("Res01",
            confirmReservationRequest);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getStatus())
            .isEqualTo("✅ Đã được quản lý nhà hàng xác nhận bàn");
    }

    @Test
    void cancelReservation_success() {
        // Mock repository
        when(reservationRepository.findById(anyString())).thenReturn(Optional.of(reservation));
        reservation.setReservationTime(LocalDateTime.now().plusHours(2));

        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        String result = reservationService.cancleReservation("Res01");

        Assertions.assertThat(result).isEqualTo(StatusReservation.TABLE_CANCLE.getDescription());
    }

    @Test
    void cancelReservation_outOfTime_throwsException() {
        // Mock repository
        when(reservationRepository.findById(anyString())).thenReturn(Optional.of(reservation));
        reservation.setReservationTime(LocalDateTime.now().plusMinutes(30));

        Assertions.assertThatThrownBy(() -> reservationService.cancleReservation("Res01"))
            .isInstanceOf(AppException.class)
            .hasMessage(ErrorCode.OUTTIME_CANCLE.getMessage());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCancelReservation_success() {
        // Mock repository
        when(reservationRepository.findById(anyString())).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        String result = reservationService.adminCancleReservation("Res01");

        Assertions.assertThat(result)
            .isEqualTo(StatusReservation.TABLE_CANCLE_ADMIN.getDescription());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllReservation_success() {
        // Mock data
        when(reservationRepository.findAll()).thenReturn(List.of(reservation));
        when(reservationMapper.toReservationResponse(any(Reservation.class))).thenReturn(
            reservationResponse);

        List<ReservationResponse> result = reservationService.getAllReservation();

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result).hasSize(1);
        Assertions.assertThat(result.get(0).getId()).isEqualTo("Res01");
    }

    @Test
    void getReservationByUserId_success() {
        // Mock security context
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("user1");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Mock repositories
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(reservationRepository.findAll()).thenReturn(List.of(reservation));
        when(reservationMapper.toReservationResponse(any(Reservation.class))).thenReturn(
            reservationResponse);

        List<ReservationResponse> result = reservationService.getReservationByUserId();

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result).hasSize(1);
        Assertions.assertThat(result.get(0).getId()).isEqualTo("Res01");
        Assertions.assertThat(result.get(0).getStatus())
            .isEqualTo(StatusReservation.UNCONFIRMED.getDescription());
    }

    @Test
    void getReservationByUserId_userNotFound() {
        // Mock security context
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("nonexistentUser");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Mock user repository to return an empty optional
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Expect an exception to be thrown
        Assertions.assertThatThrownBy(() -> reservationService.getReservationByUserId())
            .isInstanceOf(AppException.class)
            .hasMessage(ErrorCode.USER_NOT_EXISTED.getMessage());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void gotTable_success() {
        // Mock repository
        when(reservationRepository.findById(anyString())).thenReturn(Optional.of(reservation));
        reservation.setTable(diningTable);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        reservationResponse.setStatus(StatusReservation.GOT_TABLE.getDescription());
        when(reservationMapper.toReservationResponse(any(Reservation.class))).thenReturn(reservationResponse);

        ReservationResponse result = reservationService.gotTable("Res01");

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getStatus()).isEqualTo("✨ Khách đã nhận bàn");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void gotTable_noTableAssigned_throwsException() {
        // Mock repository
        when(reservationRepository.findById(anyString())).thenReturn(Optional.of(reservation));
        reservation.setTable(null);

        Assertions.assertThatThrownBy(() -> reservationService.gotTable("Res01"))
            .isInstanceOf(AppException.class)
            .hasMessage(ErrorCode.NOT_HAVE_TABLE.getMessage());
    }
}
