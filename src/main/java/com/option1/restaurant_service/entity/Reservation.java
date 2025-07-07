package com.option1.restaurant_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    LocalDateTime requestTime;      // Thời gian khách gửi yêu cầu đặt bàn
    LocalDateTime reservationTime;  // Thời gian khách muốn đến nhận bàn
    int quantityPeople;
    String messenger;               // Tin nhắn ghi chú

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    Restaurant restaurant; // CHi nhánh nhà hàng khách muốn đặt bàn

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    String status; // Trạng thái bàn: unconfirmed, confirmed, table booked, table cancelled.
    @ManyToOne
    @JoinColumn(name = "table_id")
    DiningTable table;
}
