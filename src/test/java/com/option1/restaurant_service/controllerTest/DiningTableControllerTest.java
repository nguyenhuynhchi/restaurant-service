package com.option1.restaurant_service.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.option1.restaurant_service.dto.request.TableRequest;
import com.option1.restaurant_service.dto.response.TableResponse;
import com.option1.restaurant_service.service.restaurantService.DiningTableService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class DiningTableControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DiningTableService tableService;

    private TableRequest tableRequest;
    private TableResponse tableResponse;

    @BeforeEach
    void init() {
        tableRequest = TableRequest.builder()
            .id("R01.01")
            .capacity(4)
            .description("Table near the window")
            .build();

        tableResponse = TableResponse.builder()
            .id("R01.01")
            .capacity(4)
            .description("Table near the window")
            .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createTable_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(tableRequest);
        when(tableService.createTables(ArgumentMatchers.any())).thenReturn(tableResponse);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/table")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
            .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("R01.01"))
            .andExpect(MockMvcResultMatchers.jsonPath("result.capacity").value(4))
            .andExpect(MockMvcResultMatchers.jsonPath("result.description")
                .value("Table near the window"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllTables_success() throws Exception {
        // GIVEN
        List<TableResponse> tables = List.of(tableResponse);
        when(tableService.getAllTable()).thenReturn(tables);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/table")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
            .andExpect(MockMvcResultMatchers.jsonPath("result[0].id").value("R01.01"))
            .andExpect(MockMvcResultMatchers.jsonPath("result[0].capacity").value(4))
            .andExpect(MockMvcResultMatchers.jsonPath("result[0].description")
                .value("Table near the window"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteTable_success() throws Exception {
        // GIVEN
        String tableId = "R01.01";
        doNothing().when(tableService).deleteTable(tableId);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/table/delete/{idTable}", tableId)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
            .andExpect(MockMvcResultMatchers.jsonPath("result").value("Have been delete table"));
    }
}
