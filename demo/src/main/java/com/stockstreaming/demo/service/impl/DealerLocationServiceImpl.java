package com.stockstreaming.demo.service.impl;

import com.stockstreaming.demo.dto.DealerLocationCreateRequest;
import com.stockstreaming.demo.dto.DealerLocationResponseDto;
import com.stockstreaming.demo.mapper.DealerLocationMapper;
import com.stockstreaming.demo.model.DealerGroup;
import com.stockstreaming.demo.model.DealerLocation;
import com.stockstreaming.demo.repository.DealerLocationRepository;
import com.stockstreaming.demo.service.DealerGroupService;
import com.stockstreaming.demo.service.DealerLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DealerLocationServiceImpl implements DealerLocationService{
    private final DealerLocationRepository dealerLocationRepository;
    private final DealerLocationMapper dealerLocationMapper;
    private final DealerGroupService dealerGroupService;



    @Override
    public DealerLocationResponseDto createDealerLocation(DealerLocationCreateRequest dealerLocationCreateRequest) {
        DealerGroup dealerGroup = dealerGroupService.getEntityById(dealerLocationCreateRequest.getDealerGroupId());
        DealerLocation dealerLocation = dealerLocationMapper.toCreateEntity(dealerLocationCreateRequest);
        dealerLocation.setDealerGroup(dealerGroup);
        DealerLocation savedDealerLocation = dealerLocationRepository.save(dealerLocation);
        return dealerLocationMapper.toResponseDto(savedDealerLocation);
    }

    /**
     * @param locationId
     * @param dealerLocationCreateRequest
     * @return
     */
    @Override
    public DealerLocationResponseDto updateDealerLocation(String locationId, DealerLocationCreateRequest dealerLocationCreateRequest) {
        return null;
    }

    /**
     * @param locationId
     * @param dealerLocationCreateRequest
     * @return
     */
    @Override
    public DealerLocationResponseDto partialUpdateDealerLocation(String locationId, DealerLocationCreateRequest dealerLocationCreateRequest) {
        return null;
    }

    /**
     * @param locationId
     * @return
     */
    @Override
    public DealerLocationResponseDto getDealerLocationById(String locationId) {
        return null;
    }

    /**
     * @param locationId
     * @return
     */
    @Override
    public DealerLocation getEntityById(String locationId) {
        return null;
    }
}
