package com.stockstreaming.demo.service;

import com.stockstreaming.demo.dto.DealerGroupCreateRequestDto;
import com.stockstreaming.demo.dto.DealerGroupRequestDto;
import com.stockstreaming.demo.dto.DealerGroupResponseDto;

import java.util.List;

public interface DealerGroupService {
    DealerGroupResponseDto createDealerGroup(DealerGroupCreateRequestDto dealerGroupCreateRequestDto);
    List<DealerGroupResponseDto> getAllDealerGroups();
    void deleteDealerGroup(String businessId);
    DealerGroupResponseDto getDealerGroupByBusinessId(String businessId);
    DealerGroupResponseDto updateDealerGroup(String businessId, DealerGroupRequestDto dealerGroupRequestDto);
    DealerGroupResponseDto partialUpdateDealerGroup(String businessId, DealerGroupRequestDto dealerGroupRequestDto);
}
