package com.stockstreaming.demo.controller;


import com.stockstreaming.demo.dto.DealerLocationCreateRequestDto;
import com.stockstreaming.demo.dto.DealerLocationRequestDto;
import com.stockstreaming.demo.dto.DealerLocationResponseDto;
import com.stockstreaming.demo.service.DealerLocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/dealer-locations")
@RequiredArgsConstructor
public class DealerLocationController {

    public final DealerLocationService dealerLocationService;


    @PostMapping
    public ResponseEntity<DealerLocationResponseDto> createDealerLocation(
            @Valid @RequestBody DealerLocationCreateRequestDto dealerLocationRequestDto) {
        DealerLocationResponseDto responseDto = dealerLocationService.createDealerLocation(dealerLocationRequestDto);
        return ResponseEntity.created(URI.create("/dealer-locations/" + responseDto.getLocationId())).body(responseDto);
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<DealerLocationResponseDto> updateDealerLocation(
            @PathVariable String locationId,
            @RequestBody @Valid DealerLocationRequestDto dealerLocationRequestDto) {
        DealerLocationResponseDto responseDto = dealerLocationService.updateDealerLocation(locationId, dealerLocationRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{locationId}")
    public ResponseEntity<DealerLocationResponseDto> partialUpdateDealerLocation(
            @PathVariable String locationId,
            @RequestBody @Valid DealerLocationRequestDto dealerLocationRequestDto) {
        DealerLocationResponseDto responseDto = dealerLocationService.partialUpdateDealerLocation(locationId, dealerLocationRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<DealerLocationResponseDto> getDealerLocationById(@PathVariable String locationId) {
        DealerLocationResponseDto responseDto = dealerLocationService.getDealerLocationById(locationId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<DealerLocationResponseDto>> getAllDealerLocations() {
        return ResponseEntity.ok().body(dealerLocationService.getDealerLocations());
    }

}
