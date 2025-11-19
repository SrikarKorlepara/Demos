package com.stockstreaming.demo.service.impl;

import com.stockstreaming.demo.dto.DealerLocationCreateRequestDto;
import com.stockstreaming.demo.dto.DealerLocationRequestDto;
import com.stockstreaming.demo.dto.DealerLocationResponseDto;
import com.stockstreaming.demo.mapper.DealerLocationMapper;
import com.stockstreaming.demo.model.DealerGroup;
import com.stockstreaming.demo.model.DealerLocation;
import com.stockstreaming.demo.repository.DealerLocationRepository;
import com.stockstreaming.demo.service.DealerGroupService;
import com.stockstreaming.demo.service.DealerLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealerLocationServiceImpl implements DealerLocationService{
    private final DealerLocationRepository dealerLocationRepository;
    private final DealerLocationMapper dealerLocationMapper;
    private final DealerGroupService dealerGroupService;



    @Override
    public DealerLocationResponseDto createDealerLocation(DealerLocationCreateRequestDto dealerLocationCreateRequestDto) {
        DealerGroup dealerGroup = dealerGroupService.getEntityById(dealerLocationCreateRequestDto.getDealerGroupId());
        DealerLocation dealerLocation = dealerLocationMapper.toCreateEntity(dealerLocationCreateRequestDto);
        dealerLocation.setDealerGroup(dealerGroup);
        DealerLocation savedDealerLocation = dealerLocationRepository.save(dealerLocation);
        return dealerLocationMapper.toResponseDto(savedDealerLocation);
    }

    /**
     * @param locationId
     * @param dealerLocationCreateRequestDto
     * @return
     */
    @Override
    public DealerLocationResponseDto updateDealerLocation(String locationId, DealerLocationRequestDto dealerLocationRequestDto) {
        DealerLocation existingDealerLocation = dealerLocationRepository.findByLocationId(locationId)
                .orElseThrow(() -> new RuntimeException("Dealer Location not found with id: " + locationId));
        DealerGroup dealerGroup = dealerGroupService.getEntityById(dealerLocationRequestDto.getDealerGroupId());
        DealerLocation updatedDealerLocation = dealerLocationMapper.toEntity(dealerLocationRequestDto);
        updatedDealerLocation.setId(existingDealerLocation.getId());
        updatedDealerLocation.setDealerGroup(dealerGroup);
        DealerLocation savedDealerLocation = dealerLocationRepository.save(updatedDealerLocation);
        return dealerLocationMapper.toResponseDto(savedDealerLocation);
    }

    /**
     * @param locationId
     * @param dealerLocationCreateRequestDto
     * @return
     */
    @Override
    public DealerLocationResponseDto partialUpdateDealerLocation(String locationId, DealerLocationRequestDto dealerLocationRequestDto) {
        DealerLocation existingDealerLocation = dealerLocationRepository.findByLocationId(locationId)
                .orElseThrow(() -> new RuntimeException("Dealer Location not found with id: " + locationId));
        dealerLocationMapper.partialUpdate(dealerLocationRequestDto, existingDealerLocation);
        if (dealerLocationRequestDto.getDealerGroupId() != null) {
            DealerGroup dealerGroup = dealerGroupService.getEntityById(dealerLocationRequestDto.getDealerGroupId());
            existingDealerLocation.setDealerGroup(dealerGroup);
        }
        DealerLocation savedDealerLocation = dealerLocationRepository.save(existingDealerLocation);
        return dealerLocationMapper.toResponseDto(savedDealerLocation);
    }

    /**
     * @param locationId
     * @return
     */
    @Override
    public DealerLocationResponseDto getDealerLocationById(String locationId) {
        DealerLocation dealerLocation = dealerLocationRepository.findByLocationId(locationId)
                .orElseThrow(() -> new RuntimeException("Dealer Location not found with id: " + locationId));
        return dealerLocationMapper.toResponseDto(dealerLocation);
    }

    /**
     * @param locationId
     * @return
     */
    @Override
    public DealerLocation getEntityById(String locationId) {
        return dealerLocationRepository.findByLocationId(locationId)
                .orElseThrow(() -> new RuntimeException("Dealer Location not found with id: " + locationId));
    }

    /**
     * @return
     */
    @Override
    public List<DealerLocationResponseDto> getDealerLocations() {
        List<DealerLocation> dealerLocations = dealerLocationRepository.findAll();
        return dealerLocationMapper.toResponseDtoList(dealerLocations);
    }


}
