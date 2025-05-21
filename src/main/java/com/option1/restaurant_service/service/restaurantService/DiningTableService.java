package com.option1.restaurant_service.service.restaurantService;

import com.option1.restaurant_service.dto.request.TableRequest;
import com.option1.restaurant_service.dto.response.TableResponse;
import com.option1.restaurant_service.exception.AppException;
import com.option1.restaurant_service.exception.ErrorCode;
import com.option1.restaurant_service.mapper.TableMapper;
import com.option1.restaurant_service.repository.TableRepository;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DiningTableService {

    TableMapper tableMapper;
    TableRepository tableRepository;

//    @PreAuthorize("hasRole('ADMIN')")
    public TableResponse createTables(TableRequest request) {
        if (tableRepository.existsById(request.getId())) {
            throw new AppException(ErrorCode.ID_TABLE_EXISTED);
        }
        com.option1.restaurant_service.entity.DiningTable table = tableMapper.toTable(request);

        return tableMapper.toTableResponse(tableRepository.save(table));
    }

//    @PreAuthorize("hasRole('ADMIN')")
    public List<TableResponse> getAllTable() {
        var table = tableRepository.findAll();
        return table.stream().map(tableMapper::toTableResponse).toList();
    }

//    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTable(String idTable) {
        tableRepository.findById(idTable)
            .orElseThrow(() -> new AppException(ErrorCode.ID_TABLE_NOT_EXISTED));
        tableRepository.deleteById(idTable);
    }
}
