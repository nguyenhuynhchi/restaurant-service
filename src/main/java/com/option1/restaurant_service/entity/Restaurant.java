package com.option1.restaurant_service.entity;

import jakarta.persistence.*;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Restaurant {

//    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    String id;

    String name;
    String address;
    String phone;
    String email;

    @OneToMany
    @JoinColumn // Ràng buộc mỗi bàn thuộc 1 nhà hàng
    Set<DiningTable> tables;
}
