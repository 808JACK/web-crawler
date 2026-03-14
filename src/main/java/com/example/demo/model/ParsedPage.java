package com.example.demo.model;

import java.util.List;

public record ParsedPage(
        String url,
        int depth,
        String title,
        String text,
        List<String> links
) {
}
