package com.stockstreaming.demo.controller;

import com.stockstreaming.demo.dto.DealerGroupCreateRequestDto;
import com.stockstreaming.demo.dto.DealerGroupRequestDto;
import com.stockstreaming.demo.dto.DealerGroupResponseDto;
import com.stockstreaming.demo.service.DealerGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dealer-groups")
@RequiredArgsConstructor
public class DealerGroupController {

    private final DealerGroupService dealerGroupService;

    @GetMapping("/{businessId}")
    public ResponseEntity<DealerGroupResponseDto> getDealerGroupByBusinessId(@PathVariable String businessId) {
        DealerGroupResponseDto dealerGroup = dealerGroupService.getDealerGroupByBusinessId(businessId);
        return ResponseEntity.ok(dealerGroup);
    }

    @GetMapping
    public ResponseEntity<List<DealerGroupResponseDto>> getAllDealerGroups() {
        List<DealerGroupResponseDto> dealerGroups = dealerGroupService.getAllDealerGroups();
        return ResponseEntity.ok(dealerGroups);
    }

    @PostMapping
    public ResponseEntity<DealerGroupResponseDto> createDealerGroup(@RequestBody @Valid DealerGroupCreateRequestDto dto) {
        DealerGroupResponseDto createdGroup = dealerGroupService.createDealerGroup(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
    }

    @PutMapping("/{businessId}")
    public ResponseEntity<DealerGroupResponseDto> updateDealerGroup(
            @PathVariable String businessId,
            @RequestBody @Valid DealerGroupRequestDto dto) {

        DealerGroupResponseDto updatedGroup = dealerGroupService.updateDealerGroup(businessId, dto);
        return ResponseEntity.ok(updatedGroup);
    }

    @PatchMapping("/{businessId}")
    public ResponseEntity<DealerGroupResponseDto> partialUpdateDealerGroup(
            @PathVariable String businessId,
            @RequestBody @Valid DealerGroupRequestDto dto) {

        DealerGroupResponseDto updatedGroup = dealerGroupService.partialUpdateDealerGroup(businessId, dto);
        return ResponseEntity.ok(updatedGroup);
    }

    @DeleteMapping("/{businessId}")
    public ResponseEntity<Void> deleteDealerGroup(@PathVariable String businessId) {
        dealerGroupService.deleteDealerGroup(businessId);
        return ResponseEntity.noContent().build();
    }
}
