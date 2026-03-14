package com.example.demo.dto;

public record SearchResult(
        String url,
        String title,
        long matchedTerms
) {
}
