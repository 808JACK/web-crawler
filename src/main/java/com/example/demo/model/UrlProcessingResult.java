package com.example.demo.model;

public record UrlProcessingResult(boolean allowed, CrawlTask task, String reason) {

    public static UrlProcessingResult allowed(CrawlTask task) {
        return new UrlProcessingResult(true, task, null);
    }

    public static UrlProcessingResult blocked(CrawlTask task, String reason) {
        return new UrlProcessingResult(false, task, reason);
    }
}
