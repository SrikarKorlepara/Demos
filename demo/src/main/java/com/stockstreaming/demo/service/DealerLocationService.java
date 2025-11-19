package com.stockstreaming.demo.service;

import com.stockstreaming.demo.dto.DealerLocationCreateRequestDto;
import com.stockstreaming.demo.dto.DealerLocationRequestDto;
import com.stockstreaming.demo.dto.DealerLocationResponseDto;
import com.stockstreaming.demo.model.DealerLocation;

import java.util.List;

public interface DealerLocationService {
    DealerLocationResponseDto createDealerLocation(DealerLocationCreateRequestDto dealerLocationCreateRequestDto);
    DealerLocationResponseDto updateDealerLocation(String locationId, DealerLocationRequestDto dealerLocationCreateRequestDto);
    DealerLocationResponseDto partialUpdateDealerLocation(String locationId, DealerLocationRequestDto dealerLocationCreateRequestDto);
    DealerLocationResponseDto getDealerLocationById(String locationId);
    DealerLocation getEntityById(String locationId);
    List<DealerLocationResponseDto> getDealerLocations();
}
