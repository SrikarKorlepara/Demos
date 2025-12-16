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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealerGroupServiceImpl  implements DealerGroupService {

    private final DealerGroupMapper dealerGroupMapper;
    private final DealerGroupRepository dealerGroupRepository;

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
    public void deleteDealerGroup(String businessId) {
        DealerGroup dealerGroup = dealerGroupRepository.findByBusinessId(businessId).orElseThrow(() ->
                new IllegalArgumentException("Dealer Group with businessId " + businessId + " does not exist."));
        dealerGroupRepository.delete(dealerGroup);
    }

    @Override
    public DealerGroupResponseDto getDealerGroupByBusinessId(String businessId) {
        DealerGroup dealerGroupOpt = this.getEntityById(businessId);
        return dealerGroupMapper.toResponseDto(dealerGroupOpt);
    }

    @Override
    public DealerGroup getEntityById(String businessId) {
        return dealerGroupRepository.findByBusinessId(businessId)
                .orElseThrow(() -> new IllegalArgumentException("Dealer Group with businessId " + businessId + " does not exist."));
    }

    @Transactional
    @Override
    public DealerGroupResponseDto updateDealerGroup(String businessId, DealerGroupRequestDto dealerGroupRequestDto) {
        DealerGroup existingDealerGroup = dealerGroupRepository.findByBusinessId(businessId)
                .orElseThrow(()-> new IllegalArgumentException("Dealer Group with businessId " + businessId + " does not exist."));
        dealerGroupMapper.updateEntity(dealerGroupRequestDto, existingDealerGroup);
        var savedDealerGroup = dealerGroupRepository.save(existingDealerGroup);
        return dealerGroupMapper.toResponseDto(savedDealerGroup);
    }

    @Transactional
    @Override
    public DealerGroupResponseDto partialUpdateDealerGroup(String businessId, DealerGroupRequestDto dealerGroupRequestDto) {
        DealerGroup existingDealerGroup = dealerGroupRepository.findByBusinessId(businessId)
                .orElseThrow(()-> new IllegalArgumentException("Dealer Group with businessId " + businessId + " does not exist."));
        dealerGroupMapper.partialUpdate(dealerGroupRequestDto, existingDealerGroup);
        var savedDealerGroup = dealerGroupRepository.save(existingDealerGroup);
        return dealerGroupMapper.toResponseDto(savedDealerGroup);
    }


}
