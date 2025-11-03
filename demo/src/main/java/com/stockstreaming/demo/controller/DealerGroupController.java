package com.stockstreaming.demo.controller;

import com.stockstreaming.demo.dto.DealerGroupResponseDto;
import com.stockstreaming.demo.service.DealerGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/dealer-groups")
@RequiredArgsConstructor
public class DealerGroupController {

    private final DealerGroupService dealerGroupService;

    @GetMapping("/{businessId}")
    public Mono<ResponseEntity<DealerGroupResponseDto>> getDealerGroupByBusinessId(String businessId) {
        DealerGroupResponseDto dealerGroup = dealerGroupService.getDealerGroupByBusinessId(businessId);
        return Mono.just(ResponseEntity.ok(dealerGroup));
    }

}
