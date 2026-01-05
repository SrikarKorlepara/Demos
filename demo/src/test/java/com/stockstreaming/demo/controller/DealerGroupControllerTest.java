package com.stockstreaming.demo.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockstreaming.demo.dto.DealerGroupCreateRequestDto;
import com.stockstreaming.demo.dto.DealerGroupRequestDto;
import com.stockstreaming.demo.dto.DealerGroupResponseDto;
import com.stockstreaming.demo.model.DealerGroup;
import com.stockstreaming.demo.security.AuthTokenFilter;
import com.stockstreaming.demo.service.DealerGroupService;
import com.stockstreaming.demo.service.query.DealerGroupQueryService;
import com.stockstreaming.demo.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import javax.print.attribute.standard.Media;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(DealerGroupController.class)
@AutoConfigureMockMvc(addFilters = false)
public class DealerGroupControllerTest {

    @MockitoBean
    private DealerGroupService dealerGroupService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private AuthTokenFilter authTokenFilter;

    @MockitoBean
    private DealerGroupQueryService dealerGroupQueryService;

    @Autowired
    private MockMvc mockMvc;

    private DealerGroupResponseDto responseDto;
    private DealerGroupCreateRequestDto createRequestDto;
    private static final String businessId  = "BUS123";

    @BeforeEach
    void setUp() {

        responseDto = DealerGroupResponseDto.builder()
                .id(1L)
                .name("Test Dealer Group")
                .businessId(businessId)
                .description("Test Description")
                .build();

        createRequestDto = DealerGroupCreateRequestDto.builder()
                .name("Test Dealer Group")
                .businessId(businessId)
                .description("Test Description")
                .build();
    }

    @Test
    @DisplayName("GET /dealer-groups/{businessId} - Should return 200 OK" )
    void shouldGetDealerGroupByBusinessId() throws Exception {

        //Arrange
        String businessId = "BUS123";
        DealerGroupResponseDto responseDto = DealerGroupResponseDto.builder()
                .id(1L)
                .businessId(businessId)
                .name("Test Dealer Group")
                .build();

        when(dealerGroupService.getDealerGroupByBusinessId(businessId)).thenReturn(responseDto);

        //Act & Assert
        mockMvc.perform(get("/dealer-groups/{businessId}", businessId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.businessId").value(businessId))
                .andExpect(jsonPath("$.name").value("Test Dealer Group"));

        verify(dealerGroupService).getDealerGroupByBusinessId(businessId);
    }

    @Test
    @DisplayName("POST /dealer-groups - Should return 201 Created" )
    void shouldCreateDealerGroup() throws Exception {
       //Arrange

        when(dealerGroupService.createDealerGroup(any(DealerGroupCreateRequestDto.class))).thenReturn(responseDto);

        //Act & Assert
        mockMvc.perform(post("/dealer-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.businessId").value(businessId));

        verify(dealerGroupService).createDealerGroup(any(DealerGroupCreateRequestDto.class));
    }

    @Test
    @DisplayName("PUT /dealer-groups/{businessId} - Should return 200 OK")
    void shouldFullUpdateDealerGroup() throws Exception{
        //Arrange
        DealerGroupRequestDto requestDto = DealerGroupRequestDto.builder()
                .id(1L)
                .name("Updated Dealer Group Name")
                .description("Updated Description")
                .businessId(businessId)
                .build();

        DealerGroupResponseDto updatedResponseDto = DealerGroupResponseDto.builder()
                .id(1L)
                .name("Updated Dealer Group Name")  // Updated name
                .description("Updated Description")  // Updated description
                .businessId(businessId)
                .build();

        when(dealerGroupService.updateDealerGroup(eq(businessId),any(DealerGroupRequestDto.class)))
                .thenReturn(updatedResponseDto);

        mockMvc.perform(
                put("/dealer-groups/{businessId}",businessId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Dealer Group Name"))
                .andExpect(jsonPath("$.description").value("Updated Description"));

    }
}
