package com.example.demo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CrawlRequest(
        @NotEmpty List<String> seedUrls,
        @Min(1) @Max(10) Integer maxDepth,
        @Min(1) @Max(100) Integer maxPages,
        @Min(1) @Max(20) Integer maxPagesPerDomain,
        @Min(1) @Max(10) Integer requestsPerSecond
) {
    public int safeMaxDepth() {
        return maxDepth == null ? 2 : maxDepth;
    }

    public int safeMaxPages() {
        return maxPages == null ? 10 : maxPages;
    }

    public int safeMaxPagesPerDomain() {
        return maxPagesPerDomain == null ? 5 : maxPagesPerDomain;
    }

    public int safeRequestsPerSecond() {
        return requestsPerSecond == null ? 2 : requestsPerSecond;
    }
}
