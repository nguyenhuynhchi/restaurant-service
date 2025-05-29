package com.option1.restaurant_service.serviceTest;

import com.option1.restaurant_service.dto.request.TableRequest;
import com.option1.restaurant_service.dto.response.TableResponse;
import com.option1.restaurant_service.entity.DiningTable;
import com.option1.restaurant_service.exception.AppException;
import com.option1.restaurant_service.exception.ErrorCode;
import com.option1.restaurant_service.mapper.TableMapper;
import com.option1.restaurant_service.repository.TableRepository;
import com.option1.restaurant_service.service.restaurantService.DiningTableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.assertj.core.api.Assertions;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@TestPropertySource("/test.properties")
public class DiningTableServiceTest {
    @MockBean
    private TableRepository tableRepository;

    @MockBean
    private TableMapper tableMapper;

    @Autowired
    private DiningTableService diningTableService;

    private TableRequest tableRequest;
    private DiningTable diningTable;
    private TableResponse tableResponse;

    @BeforeEach
    void init() {
        tableRequest = TableRequest.builder()
            .id("T01")
            .capacity(4)
            .description("Window Table")
            .build();

        diningTable = DiningTable.builder()
            .id("T01")
            .capacity(4)
            .description("Window Table")
            .build();

        tableResponse = TableResponse.builder()
            .id("T01")
            .capacity(4)
            .description("Window Table")
            .build();
    }

    @Test
    void createTable_success() {
        // Mock repository and mapper
        when(tableRepository.existsById(anyString())).thenReturn(false);
        when(tableMapper.toTable(any(TableRequest.class))).thenReturn(diningTable);
        when(tableRepository.save(any(DiningTable.class))).thenReturn(diningTable);
        when(tableMapper.toTableResponse(any(DiningTable.class))).thenReturn(tableResponse);

        TableResponse result = diningTableService.createTables(tableRequest);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo("T01");
        Assertions.assertThat(result.getCapacity()).isEqualTo(4);
        Assertions.assertThat(result.getDescription()).isEqualTo("Window Table");
    }

    @Test
    void createTable_tableAlreadyExists_throwsException() {
        // Mock repository
        when(tableRepository.existsById(anyString())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> diningTableService.createTables(tableRequest))
            .isInstanceOf(AppException.class)
            .hasMessage(ErrorCode.ID_TABLE_EXISTED.getMessage());
    }

    @Test
    void getAllTable_success() {
        // Mock repository and mapper
        when(tableRepository.findAll()).thenReturn(List.of(diningTable));
        when(tableMapper.toTableResponse(any(DiningTable.class))).thenReturn(tableResponse);

        List<TableResponse> result = diningTableService.getAllTable();

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).hasSize(1);
        Assertions.assertThat(result.get(0).getId()).isEqualTo("T01");
        Assertions.assertThat(result.get(0).getCapacity()).isEqualTo(4);
    }

    @Test
    void deleteTable_success() {
        // Mock repository
        when(tableRepository.findById(anyString())).thenReturn(Optional.of(diningTable));
        doNothing().when(tableRepository).deleteById(anyString());

        diningTableService.deleteTable("T01");

        Assertions.assertThatNoException();
    }

    @Test
    void deleteTable_tableNotFound_throwsException() {
        // Mock repository
        when(tableRepository.findById(anyString())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> diningTableService.deleteTable("T01"))
            .isInstanceOf(AppException.class)
            .hasMessage(ErrorCode.ID_TABLE_NOT_EXISTED.getMessage());
    }
}
