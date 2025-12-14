package com.stockstreaming.demo.service.impl;

import com.stockstreaming.demo.dto.DealerGroupCreateRequestDto;
import com.stockstreaming.demo.mapper.DealerGroupMapper;
import com.stockstreaming.demo.model.DealerGroup;
import com.stockstreaming.demo.repository.DealerGroupRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealerGroupServiceImplTest {


    @Mock
    DealerGroupMapper dealerGroupMapper;

    @Mock
    DealerGroupRepository dealerGroupRepository;

    @InjectMocks
    DealerGroupServiceImpl dealerGroupService;


    @BeforeAll
    public static void init(){
        System.out.println("Before All called");
    }

    @Test
    void createDealerGroupShouldAddDealerGroupSuccessfully() {
        System.out.println("Testing add product successful");
        DealerGroupCreateRequestDto dto = mock(DealerGroupCreateRequestDto.class);
        DealerGroup dealerGroup = mock(DealerGroup.class);
        when(dealerGroupRepository.existsByBusinessId(dto.getBusinessId())).thenReturn(false);
        when(dealerGroupMapper.toCreateEntity(dto)).thenReturn(dealerGroup);
        dealerGroupService.createDealerGroup(dto);
        verify(dealerGroupRepository, times(1)).save(dealerGroup);
    }

    @Test
    void getAllDealerGroups() {
    }

    @Test
    void deleteDealerGroup() {
    }

    @Test
    void getDealerGroupByBusinessId() {
    }

    @Test
    void getEntityById() {
    }

    @Test
    void updateDealerGroup() {
    }

    @Test
    void partialUpdateDealerGroup() {
    }
}