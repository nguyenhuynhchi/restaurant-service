package com.option1.restaurant_service.service.restaurantService;


import com.option1.restaurant_service.dto.request.RestaurantRequest;
import com.option1.restaurant_service.dto.response.RestaurantResponse;
import com.option1.restaurant_service.mapper.RestaurantMapper;
import com.option1.restaurant_service.repository.RestaurantRepository;
import com.option1.restaurant_service.repository.TableRepository;
import java.util.HashSet;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestaurantService {

    RestaurantMapper restaurantMapper;
    RestaurantRepository restaurantRepository;

    TableRepository tableRepository;

    public RestaurantResponse createRestaurant(RestaurantRequest request){
//        if (restaurantRepository.existsById(request.getId())) {
//            throw new AppException(ErrorCode.NAME_RESTAURANT_EXISTED);
//        }
        var restaurant = restaurantMapper.toRestaurant(request);
        var table = tableRepository.findAllById(request.getTables());

        restaurant.setTables(new HashSet<>(table));

        restaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toRestaurantResponse(restaurant);
    }

    public List<RestaurantResponse> getAllRestaurant(){
        var restaurants = restaurantRepository.findAll();
        return restaurants.stream().map(restaurantMapper::toRestaurantResponse).toList();
    }

    public void deleteRestaurant(String name){
//        RestaurantRepository.findById(name).orElseThrow(() -> new AppException(ErrorCode.NAME_RESTAURANT_NOT_EXISTED));
        restaurantRepository.deleteById(name);
    }
}
