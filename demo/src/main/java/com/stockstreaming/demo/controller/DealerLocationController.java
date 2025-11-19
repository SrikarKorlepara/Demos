package com.stockstreaming.demo.controller;


import com.stockstreaming.demo.dto.DealerLocationRequestDto;
import com.stockstreaming.demo.service.DealerLocationService;
import com.stockstreaming.demo.service.impl.DealerLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dealer-locations")
@RequiredArgsConstructor
public class DealerLocationController {

    public final DealerLocationService dealerLocationService;


    @GetMapping
    public DealerLocationRequestDto getDealerLocations() {
        return null;
    }

}
