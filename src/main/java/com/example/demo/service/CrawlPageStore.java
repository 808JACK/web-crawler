package com.example.demo.service;

import com.example.demo.model.ProcessedPage;

public interface CrawlPageStore {

    boolean existsByUrl(String url);

    boolean existsByContentHash(String contentHash);

    void save(ProcessedPage page);
}
