package com.example.demo.model;

import com.example.demo.service.CrawlQueue;
import com.example.demo.service.InMemoryInvertedIndexer;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CrawlContext {

    private final int maxDepth;
    private final int maxPages;
    private final int maxPagesPerDomain;
    private final int requestsPerSecond;
    private final CrawlQueue crawlQueue;
    private final InMemoryInvertedIndexer invertedIndexer;
    private final Set<String> seenUrls = new HashSet<>();
    private final Set<String> resolvedUrls = new HashSet<>();
    private final Set<String> seenContentHashes = new HashSet<>();
    private final Map<String, Integer> domainPageCounts = new HashMap<>();
    private final Map<String, Long> domainLastAccessAt = new HashMap<>();
    private int processedPages;

    public CrawlContext(
            int maxDepth,
            int maxPages,
            int maxPagesPerDomain,
            int requestsPerSecond,
            CrawlQueue crawlQueue,
            InMemoryInvertedIndexer invertedIndexer
    ) {
        this.maxDepth = maxDepth;
        this.maxPages = maxPages;
        this.maxPagesPerDomain = maxPagesPerDomain;
        this.requestsPerSecond = requestsPerSecond;
        this.crawlQueue = crawlQueue;
        this.invertedIndexer = invertedIndexer;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public CrawlQueue getCrawlQueue() {
        return crawlQueue;
    }

    public InMemoryInvertedIndexer getInvertedIndexer() {
        return invertedIndexer;
    }

    public boolean markUrl(String url) {
        return seenUrls.add(url);
    }

    public boolean markResolvedUrl(String url) {
        return resolvedUrls.add(url);
    }

    public boolean markContentHash(String hash) {
        return seenContentHashes.add(hash);
    }

    public boolean canVisitDomain(String domain) {
        return domainPageCounts.getOrDefault(domain, 0) < maxPagesPerDomain;
    }

    public void markDomainVisit(String domain) {
        domainPageCounts.merge(domain, 1, Integer::sum);
    }

    public long requestIntervalMillis() {
        return 1000L / requestsPerSecond;
    }

    public long lastAccessAt(String domain) {
        return domainLastAccessAt.getOrDefault(domain, 0L);
    }

    public void updateLastAccessAt(String domain, long value) {
        domainLastAccessAt.put(domain, value);
    }

    public boolean canProcessMore() {
        return processedPages < maxPages;
    }

    public void incrementProcessedPages() {
        processedPages++;
    }

    public int getProcessedPages() {
        return processedPages;
    }
}
