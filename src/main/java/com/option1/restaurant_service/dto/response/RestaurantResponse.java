package com.option1.restaurant_service.dto.response;

import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantResponse {
    String id;
    String name;
    String address;
    String phone;
    String email;

    Set<TableResponse> tables;
}
