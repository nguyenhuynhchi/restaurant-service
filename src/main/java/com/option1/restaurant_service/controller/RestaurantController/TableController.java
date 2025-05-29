package com.option1.restaurant_service.controller.RestaurantController;

import com.option1.restaurant_service.dto.response.ApiResponse;
import com.option1.restaurant_service.dto.request.TableRequest;
import com.option1.restaurant_service.dto.response.TableResponse;
import com.option1.restaurant_service.service.restaurantService.DiningTableService;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/table")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TableController {

    DiningTableService tableService;


    @PostMapping
    ApiResponse<TableResponse> createTable(@RequestBody TableRequest request){
        return ApiResponse.<TableResponse>builder()
            .result(tableService.createTables(request))
            .build();
    }

    @GetMapping
    ApiResponse<List<TableResponse>> getAllTables(){
        return ApiResponse.<List<TableResponse>>builder()
            .result(tableService.getAllTable())
            .build();
    }

    @DeleteMapping("/delete/{idTable}")
    ApiResponse<String> deleteTable(@PathVariable String idTable){
        tableService.deleteTable(idTable);
        return ApiResponse.<String>builder()
            .result("Have been delete table").build();
    }
}
