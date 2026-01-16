package com.stockstreaming.demo.service.impl;

import com.stockstreaming.demo.dto.DealerGroupCreateRequestDto;
import com.stockstreaming.demo.dto.DealerGroupRequestDto;
import com.stockstreaming.demo.dto.DealerGroupResponseDto;
import com.stockstreaming.demo.mapper.DealerGroupMapper;
import com.stockstreaming.demo.model.DealerGroup;
import com.stockstreaming.demo.repository.DealerGroupRepository;
import com.stockstreaming.demo.service.DealerGroupService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealerGroupServiceImpl  implements DealerGroupService {

    private final DealerGroupMapper dealerGroupMapper;
    private final DealerGroupRepository dealerGroupRepository;
    private final CacheInspectionService cacheInspectionService;

    @Transactional
    @Override
    public DealerGroupResponseDto createDealerGroup(DealerGroupCreateRequestDto dealerGroupCreateRequestDto) {
        if(dealerGroupRepository.existsByBusinessId(dealerGroupCreateRequestDto.getBusinessId())) {
            throw new IllegalArgumentException("Dealer Group with businessId " + dealerGroupCreateRequestDto.getBusinessId() + " already exists.");
        }
        var dealerGroup = dealerGroupMapper.toCreateEntity(dealerGroupCreateRequestDto);
        var savedDealerGroup = dealerGroupRepository.save(dealerGroup);
        return dealerGroupMapper.toResponseDto(savedDealerGroup);
    }

    @Override
    public List<DealerGroupResponseDto> getAllDealerGroups() {
        var dealerGroups = dealerGroupRepository.findAll();
        return dealerGroupMapper.toResponseDtoList(dealerGroups);
    }

    @Transactional
    @Override
    @CacheEvict(value = "dealerGroupEntity", key = "#businessId" , beforeInvocation = true)
    public void deleteDealerGroup(String businessId) {
        DealerGroup dealerGroup = dealerGroupRepository.findByBusinessId(businessId).orElseThrow(() ->
                new IllegalArgumentException("Dealer Group with businessId " + businessId + " does not exist."));
        dealerGroupRepository.delete(dealerGroup);
    }

    @Override
    public DealerGroupResponseDto getDealerGroupByBusinessId(String businessId) {
        DealerGroup dealerGroupOpt = this.getEntityById(businessId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Dealer Group with businessId " + businessId + " does not exist."));
        return dealerGroupMapper.toResponseDto(dealerGroupOpt);
    }

    @Override
    @Cacheable(value = "dealerGroupEntity", key = "#businessId")
    public Optional<DealerGroup> getEntityById(String businessId) {
        log.info("Fetching DealerGroup entity with businessId {} from database.", businessId);
        return dealerGroupRepository.findByBusinessId(businessId);
    }

    @Override
    public DealerGroupResponseDto updateDealerGroup(String businessId, DealerGroupRequestDto dto) {
        DealerGroup updated = updateDealerGroupInternal(businessId, dto);
        return dealerGroupMapper.toResponseDto(updated);
    }


    @Transactional
    @CachePut(value = "dealerGroupEntity", key = "#businessId")
    protected DealerGroup updateDealerGroupInternal(String businessId, DealerGroupRequestDto dto) {
        DealerGroup existingDealerGroup = dealerGroupRepository.findByBusinessId(businessId)
                .orElseThrow(() -> new IllegalArgumentException("Dealer Group with businessId " + businessId + " does not exist."));

        dealerGroupMapper.updateEntity(dto, existingDealerGroup);
        return dealerGroupRepository.save(existingDealerGroup);
    }

    @Override
    public DealerGroupResponseDto partialUpdateDealerGroup(String businessId, DealerGroupRequestDto dto) {
        DealerGroup updated = partialUpdateDealerGroupInternal(businessId, dto);
        return dealerGroupMapper.toResponseDto(updated);
    }

    @Transactional
    @CachePut(value = "dealerGroupEntity", key = "#businessId")
    protected DealerGroup partialUpdateDealerGroupInternal(String businessId, DealerGroupRequestDto dto) {
        DealerGroup existingDealerGroup = dealerGroupRepository.findByBusinessId(businessId)
                .orElseThrow(() -> new IllegalArgumentException("Dealer Group with businessId " + businessId + " does not exist."));

        dealerGroupMapper.partialUpdate(dto, existingDealerGroup);
        return dealerGroupRepository.save(existingDealerGroup);
    }



}
