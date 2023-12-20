package com.boha.skunk.services;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class PricingPlanService {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String apiKey) {
        return cache.computeIfAbsent(apiKey, this::newBucket);
    }

    private Bucket newBucket(String apiKey) {
        Bandwidth limit = Bandwidth.simple(2, Duration.of(1, ChronoUnit.MINUTES));
        if (apiKey.contains("Plan1")) {
            limit = Bandwidth.simple(5, Duration.of(1, ChronoUnit.MINUTES));
        }
        if (apiKey.contains("Plan2")) {
            limit = Bandwidth.simple(10, Duration.of(1, ChronoUnit.MINUTES));
        }
        if (apiKey.contains("Plan3")) {
            limit = Bandwidth.simple(50, Duration.of(1, ChronoUnit.MINUTES));
        }
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}
