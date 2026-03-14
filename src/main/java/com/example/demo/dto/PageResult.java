package com.example.demo.dto;

import java.util.List;

public record PageResult(
        String url,
        int depth,
        String title,
        String contentHash,
        List<String> discoveredLinks
) {
}
