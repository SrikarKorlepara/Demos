package com.stockstreaming.demo.controller;

import com.stockstreaming.demo.service.impl.CacheInspectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cache")
@RequiredArgsConstructor
public class CacheController {

    private final CacheInspectionService cacheInspectionService;

    @GetMapping("/contents")
    public void getCacheContents(@RequestParam("cacheName") String cacheName){
        cacheInspectionService.printCacheContents(cacheName);
    }
}
