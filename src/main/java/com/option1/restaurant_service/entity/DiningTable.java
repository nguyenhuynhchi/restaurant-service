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
public class DiningTable {
    @Id
    String id;
    int capacity;
    String description;
//    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL)
//    private Set<Reservation> reservations;

//    @ManyToOne
//    @JoinColumn(name = "restaurant_id")
//    private Restaurant restaurant;

}
