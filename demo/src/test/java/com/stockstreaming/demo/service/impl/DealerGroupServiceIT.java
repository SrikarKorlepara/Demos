package com.stockstreaming.demo.service.impl;


import com.stockstreaming.demo.dto.DealerGroupCreateRequestDto;
import com.stockstreaming.demo.dto.DealerGroupRequestDto;
import com.stockstreaming.demo.dto.DealerGroupResponseDto;
import com.stockstreaming.demo.model.DealerGroup;
import com.stockstreaming.demo.repository.DealerGroupRepository;
import com.stockstreaming.demo.service.DealerGroupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("DealerGroup Service Integration Tests")
public class DealerGroupServiceIT {

    @Autowired
    private DealerGroupService dealerGroupService;

    @Autowired
    private DealerGroupRepository dealerGroupRepository;


    @Test
    @DisplayName("Should create and fetch a Dealer Group successfully")
    void shouldCreateAndFetchDealerGroup(){
        DealerGroupCreateRequestDto createRequestDto = DealerGroupCreateRequestDto
                .builder()
                .businessId("BG12345")
                .name("Best Group")
                .build();
        DealerGroupResponseDto responseDto = dealerGroupService.createDealerGroup(createRequestDto);

        assertNotNull(responseDto);

        DealerGroup fetchedEntity = dealerGroupRepository.findByBusinessId("BG12345").orElseThrow();

        assertNotNull(fetchedEntity);
        assertEquals("BG12345", fetchedEntity.getBusinessId());
    }

    @Test
    @DisplayName("Should throw exception when creating duplicate Dealer Group")
    void shouldThrowExceptionWhenCreatingDuplicateDealerGroup(){
        DealerGroupCreateRequestDto createRequestDto = DealerGroupCreateRequestDto
                .builder()
                .businessId("BG12345")
                .name("Best Group")
                .build();
        dealerGroupService.createDealerGroup(createRequestDto);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                ()-> dealerGroupService.createDealerGroup(createRequestDto)
        );

        assertTrue(ex.getMessage().contains("already exists"));
    }

    @Test
    @DisplayName("Should update dealer Group successfully")
    void shouldUpdateDealerGroup(){
        dealerGroupService.createDealerGroup(DealerGroupCreateRequestDto
                .builder()
                .businessId("BG12345")
                .name("Best Group")
                .build()
        );

        DealerGroupRequestDto updateRequestDto = DealerGroupRequestDto.builder()
                .name("Updated Group Name")
                .description("Updated Description")
                .build();

        DealerGroupResponseDto updatedDto = dealerGroupService.updateDealerGroup("BG12345", updateRequestDto);

        assertEquals("Updated Group Name", updatedDto.getName());
        assertNotNull(updatedDto.getDescription());

        DealerGroup fetchedEntity = dealerGroupRepository.findByBusinessId("BG12345").orElseThrow();

        assertEquals("Updated Group Name", fetchedEntity.getName());
        assertEquals("Updated Description", fetchedEntity.getDescription());
    }

    void shouldPartiallyUpdateDealerGroup(){
        dealerGroupService.createDealerGroup(DealerGroupCreateRequestDto
                .builder()
                .businessId("BG12345")
                .name("Best Group")
                .build()
        );

        DealerGroupRequestDto partialUpdateRequestDto = DealerGroupRequestDto.builder()
                .description("Partially Updated Description")
                .build();

        DealerGroupResponseDto updatedDto = dealerGroupService.partialUpdateDealerGroup("BG12345", partialUpdateRequestDto);
        assertEquals("Best Group", updatedDto.getName());
        assertEquals("Partially Updated Description", updatedDto.getDescription());

        DealerGroup fetchedEntity = dealerGroupRepository.findByBusinessId("BG12345").orElseThrow();
        assertEquals("Best Group", fetchedEntity.getName());
        assertEquals("Partially Updated Description", fetchedEntity.getDescription());
    }
}
