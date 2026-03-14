package com.example.demo.service;

import com.example.demo.dto.PageResult;
import com.example.demo.model.CrawlContext;
import com.example.demo.model.CrawlTask;
import com.example.demo.model.ProcessedPage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CrawlWorker {

    private final CrawlStepExecutor crawlStepExecutor;

    public CrawlWorker(CrawlStepExecutor crawlStepExecutor) {
        this.crawlStepExecutor = crawlStepExecutor;
    }

    public List<PageResult> work(CrawlContext context) {
        List<PageResult> results = new ArrayList<>();
        CrawlQueue crawlQueue = context.getCrawlQueue();

        while (!crawlQueue.isEmpty() && context.canProcessMore()) {
            CrawlTask task = crawlQueue.poll().orElseThrow();
            crawlStepExecutor.execute(task, context).ifPresent(execution -> {
                ProcessedPage page = execution.page();
                results.add(new PageResult(page.url(), page.depth(), page.title(), page.contentHash(), page.links()));
                execution.nextTasks().forEach(crawlQueue::add);
            });
        }

        return results;
    }
}
