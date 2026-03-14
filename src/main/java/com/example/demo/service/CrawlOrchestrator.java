package com.example.demo.service;

import com.example.demo.dto.CrawlRequest;
import com.example.demo.dto.CrawlResponse;
import com.example.demo.dto.PageResult;
import com.example.demo.model.CrawlContext;
import com.example.demo.model.CrawlTask;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CrawlOrchestrator {

    private final CrawlWorker crawlWorker;

    public CrawlOrchestrator(CrawlWorker crawlWorker) {
        this.crawlWorker = crawlWorker;
    }

    public CrawlResponse crawl(CrawlRequest request) {
        CrawlQueue crawlQueue = new CrawlQueue();
        InMemoryInvertedIndexer invertedIndexer = new InMemoryInvertedIndexer();

        CrawlContext context = new CrawlContext(
                request.safeMaxDepth(),
                request.safeMaxPages(),
                request.safeMaxPagesPerDomain(),
                request.safeRequestsPerSecond(),
                crawlQueue,
                invertedIndexer
        );
        List<CrawlTask> seeds = request.seedUrls().stream()
                .map(url -> new CrawlTask(url, 0))
                .toList();
        crawlQueue.reset(seeds);

        List<PageResult> results = crawlWorker.work(context);
        return new CrawlResponse(context.getProcessedPages(), results, invertedIndexer.snapshot(10));
    }
}
