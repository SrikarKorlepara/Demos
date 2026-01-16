package com.stockstreaming.demo.service.impl;

import com.github.benmanes.caffeine.cache.LoadingCache;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DealerLocationServiceImpl implements DealerLocationService{
    private final DealerLocationRepository dealerLocationRepository;
    private final DealerLocationMapper dealerLocationMapper;
    private final DealerGroupService dealerGroupService;
    private final LoadingCache<String, Optional<DealerLocation>> dealerLocationCache;



    @Override
    public DealerLocationResponseDto createDealerLocation(DealerLocationCreateRequestDto dealerLocationCreateRequestDto) {
        DealerGroup dealerGroup = dealerGroupService.getEntityById(dealerLocationCreateRequestDto.getDealerGroupId()).orElseThrow(()-> new IllegalArgumentException("Dealer Group not found with id: " + dealerLocationCreateRequestDto.getDealerGroupId()));
        DealerLocation dealerLocation = dealerLocationMapper.toCreateEntity(dealerLocationCreateRequestDto);
        dealerLocation.setDealerGroup(dealerGroup);
        DealerLocation savedDealerLocation = dealerLocationRepository.save(dealerLocation);
        dealerLocationCache.invalidate(savedDealerLocation.getLocationId());
        return dealerLocationMapper.toResponseDto(savedDealerLocation);
    }

    @Override
    public DealerLocationResponseDto updateDealerLocation(String locationId, DealerLocationRequestDto dealerLocationRequestDto) {
        DealerLocation existingDealerLocation = dealerLocationRepository.findByLocationId(locationId)
                .orElseThrow(() -> new RuntimeException("Dealer Location not found with id: " + locationId));
        DealerGroup dealerGroup = dealerGroupService.getEntityById(dealerLocationRequestDto.getDealerGroupId())
                .orElseThrow(()-> new RuntimeException("Dealer Group not found with id: " + dealerLocationRequestDto.getDealerGroupId()));
        DealerLocation updatedDealerLocation = dealerLocationMapper.toEntity(dealerLocationRequestDto);
        updatedDealerLocation.setId(existingDealerLocation.getId());
        updatedDealerLocation.setDealerGroup(dealerGroup);
        DealerLocation savedDealerLocation = dealerLocationRepository.save(updatedDealerLocation);
        dealerLocationCache.invalidate(locationId);
        return dealerLocationMapper.toResponseDto(savedDealerLocation);
    }

    @Override
    public DealerLocationResponseDto partialUpdateDealerLocation(String locationId, DealerLocationRequestDto dealerLocationRequestDto) {
        DealerLocation existingDealerLocation = dealerLocationRepository.findByLocationId(locationId)
                .orElseThrow(() -> new RuntimeException("Dealer Location not found with id: " + locationId));
        dealerLocationMapper.partialUpdate(dealerLocationRequestDto, existingDealerLocation);
        if (dealerLocationRequestDto.getDealerGroupId() != null) {
            DealerGroup dealerGroup = dealerGroupService.getEntityById(dealerLocationRequestDto.getDealerGroupId()).orElseThrow(
                    () -> new IllegalArgumentException("Dealer Group not found with id: " + dealerLocationRequestDto.getDealerGroupId()));
            existingDealerLocation.setDealerGroup(dealerGroup);
        }
        DealerLocation savedDealerLocation = dealerLocationRepository.save(existingDealerLocation);
        dealerLocationCache.invalidate(locationId);
        return dealerLocationMapper.toResponseDto(savedDealerLocation);
    }

    /**
     * @param locationId
     * @return
     */
    @Override
    public DealerLocationResponseDto getDealerLocationById(String locationId) {
        DealerLocation dealerLocation = this.getEntityById(locationId)
                .orElseThrow(() -> new RuntimeException("Dealer Location not found with id: " + locationId));
        return dealerLocationMapper.toResponseDto(dealerLocation);
    }

    /**
     * @param locationId
     * @return
     */
    @Override
    public Optional<DealerLocation> getEntityById(String locationId) {
        return dealerLocationCache.get(locationId);
    }

    /**
     * @return
     */
    @Override
    public List<DealerLocationResponseDto> getDealerLocations() {
        List<DealerLocation> dealerLocations = dealerLocationRepository.findAll();
        return dealerLocationMapper.toResponseDtoList(dealerLocations);
    }

    @Override
    public void deleteDealerLocation(String locationId) {

        DealerLocation existingDealerLocation =
                dealerLocationRepository.findByLocationId(locationId)
                        .orElseThrow(() ->
                                new RuntimeException("Dealer Location not found with id: " + locationId));
        dealerLocationCache.invalidate(locationId);
        dealerLocationRepository.delete(existingDealerLocation);


    }
}
