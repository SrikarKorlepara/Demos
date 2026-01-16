package com.stockstreaming.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CacheInspectionService {

    private final CacheManager cacheManager;

    public void printCacheContents(String cacheName) {
        var cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            System.out.println("Contents of cache '" + cacheName + "':");
            var nativeCache = cache.getNativeCache();
            System.out.println(Objects.requireNonNull(nativeCache).toString());
        } else {
            System.out.println("Cache '" + cacheName + "' does not exist.");
        }
    }
}
