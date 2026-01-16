package com.stockstreaming.demo.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.stockstreaming.demo.model.DealerLocation;
import com.stockstreaming.demo.repository.DealerLocationRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.index.qual.NonNegative;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@Slf4j
public class CacheConfig {

    private static final int BASE_TTL_SECONDS = 300;
    private static final int JITTER_SECONDS = 30;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager("dealerGroupEntity");

        manager.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfter
                                (new Expiry<>() {
                                    // Fixed TTL with jitter to prevent cache stampede.
                                    @Override
                                    public long expireAfterCreate(Object o, Object o2, long l) {
                                        return TimeUnit.SECONDS.toNanos(BASE_TTL_SECONDS + JITTER_SECONDS);
                                    }
                                    // TTL does not extend on read or update.
                                    @Override
                                    public long expireAfterUpdate(Object o, Object o2, long l, @NonNegative long l1) {
                                        return l1;
                                    }
                                    // TTL does not extend on read or update.
                                    @Override
                                    public long expireAfterRead(Object o, Object o2, long l, @NonNegative long l1) {
                                        return l1;
                                    }
                                })
                        .maximumSize(10_000)
                        .recordStats()
        );
        log.info("CacheManager configured with Caffeine cache {}", manager.getClass().getName());

        return manager;
    }

    @Bean
    public LoadingCache<String, Optional<DealerLocation>> dealerLocationCache(
            DealerLocationRepository repository) {

        return Caffeine.newBuilder()
                .expireAfter
                        (new Expiry<>() {
                            // Fixed TTL with jitter to prevent cache stampede.
                            @Override
                            public long expireAfterCreate(Object o, Object o2, long l) {

                                return TimeUnit.SECONDS.toNanos(BASE_TTL_SECONDS + JITTER_SECONDS);
                            }
                            // TTL does not extend on read or update.
                            @Override
                            public long expireAfterUpdate(Object o, Object o2, long l, @NonNegative long l1) {
                                return l1;
                            }
                            // TTL does not extend on read or update.
                            @Override
                            public long expireAfterRead(Object o, Object o2, long l, @NonNegative long l1) {
                                return l1;
                            }
                        })
                .maximumSize(10_000)
                .recordStats()
                .build(locationId -> {
                    log.info("DB HIT for {}", locationId);
                    return repository.findByLocationId(locationId);
                });
    }

}
