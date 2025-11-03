package com.stockstreaming.demo.controller;

import com.stockstreaming.demo.dto.DealerGroupCreateRequestDto;
import com.stockstreaming.demo.dto.DealerGroupRequestDto;
import com.stockstreaming.demo.dto.DealerGroupResponseDto;
import com.stockstreaming.demo.service.DealerGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/dealer-groups")
@RequiredArgsConstructor
public class DealerGroupController {

    private final DealerGroupService dealerGroupService;

    @GetMapping("/{businessId}")
    public Mono<ResponseEntity<DealerGroupResponseDto>> getDealerGroupByBusinessId(@PathVariable String businessId) {
        DealerGroupResponseDto dealerGroup = dealerGroupService.getDealerGroupByBusinessId(businessId);
        return Mono.just(ResponseEntity.ok(dealerGroup));
    }

    @PostMapping
    public Mono<ResponseEntity<DealerGroupResponseDto>> createDealerGroup(@RequestBody Mono<DealerGroupCreateRequestDto> dealerGroupRequestDtoMono) {
        return dealerGroupRequestDtoMono
                .map(dealerGroupService::createDealerGroup)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{businessId}")
    public Mono<ResponseEntity<DealerGroupResponseDto>> updateDealerGroup(
            @PathVariable String businessId,
            @RequestBody Mono<DealerGroupRequestDto> dealerGroupRequestDtoMono
    ) {
        return dealerGroupRequestDtoMono
                .map(dto -> dealerGroupService.updateDealerGroup(businessId,dto))
                .map(ResponseEntity::ok);
    }

    @PatchMapping("/{businessId}")
    public Mono<ResponseEntity<DealerGroupResponseDto>> partialUpdateDealerGroup(
            @PathVariable("businessId") String businessId,
            @RequestBody Mono<DealerGroupRequestDto> dealerGroupRequestDtoMono
    ) {
        return dealerGroupRequestDtoMono
                .map(dto -> dealerGroupService.partialUpdateDealerGroup(businessId,dto))
                .map(ResponseEntity::ok);
    }
}
