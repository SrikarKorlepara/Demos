package com.stockstreaming.demo.service;

import com.stockstreaming.demo.dto.DealerLocationCreateRequest;
import com.stockstreaming.demo.dto.DealerLocationResponseDto;
import com.stockstreaming.demo.model.DealerLocation;

public interface DealerLocationService {
    DealerLocationResponseDto createDealerLocation(DealerLocationCreateRequest dealerLocationCreateRequest);
    DealerLocationResponseDto updateDealerLocation(String locationId, DealerLocationCreateRequest dealerLocationCreateRequest);
    DealerLocationResponseDto partialUpdateDealerLocation(String locationId, DealerLocationCreateRequest dealerLocationCreateRequest);
    DealerLocationResponseDto getDealerLocationById(String locationId);
    DealerLocation getEntityById(String locationId);
}
