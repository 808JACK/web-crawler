package com.example.demo.dto;

import java.util.List;

public record SearchResponse(
        String query,
        List<SearchResult> results
) {
}
