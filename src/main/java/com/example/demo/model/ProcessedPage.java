package com.example.demo.model;

import java.util.List;

public record ProcessedPage(
        String url,
        int depth,
        String title,
        String text,
        String contentHash,
        List<String> links
) {
}
