package com.option1.restaurant_service.mapper;

import com.option1.restaurant_service.dto.request.RestaurantRequest;
import com.option1.restaurant_service.dto.response.RestaurantResponse;
import com.option1.restaurant_service.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    @Mapping(target = "tables", ignore = true)
    Restaurant toRestaurant(RestaurantRequest request);

    RestaurantResponse toRestaurantResponse(Restaurant restaurant);
}
