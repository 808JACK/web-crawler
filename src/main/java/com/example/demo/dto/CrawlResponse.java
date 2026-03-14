package com.example.demo.dto;

import java.util.List;
import java.util.Map;

public record CrawlResponse(
        int processedPages,
        List<PageResult> pages,
        Map<String, List<String>> invertedIndex
) {
}
