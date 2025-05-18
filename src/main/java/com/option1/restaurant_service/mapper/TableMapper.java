package com.option1.restaurant_service.mapper;

import com.option1.restaurant_service.dto.request.TableRequest;
import com.option1.restaurant_service.dto.response.TableResponse;
import com.option1.restaurant_service.entity.DiningTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TableMapper {

    DiningTable toTable(TableRequest request);

    TableResponse toTableResponse(DiningTable diningTable);

}
