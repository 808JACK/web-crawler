package com.example.demo.workflow.postprocessor;

import com.example.demo.model.CrawlContext;
import com.example.demo.model.ProcessedPage;
import com.example.demo.service.CrawlPageStore;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(30)
public class DuplicateContentPostProcessor implements ContentPostProcessor {

    private final CrawlPageStore crawlPageStore;

    public DuplicateContentPostProcessor(CrawlPageStore crawlPageStore) {
        this.crawlPageStore = crawlPageStore;
    }

    @Override
    public ProcessedPage process(ProcessedPage page, CrawlContext context) {
        boolean firstOccurrence = context.markContentHash(page.contentHash())
                && !crawlPageStore.existsByContentHash(page.contentHash());
        if (firstOccurrence) {
            return page;
        }

        return new ProcessedPage(
                page.url(),
                page.depth(),
                page.title(),
                page.text(),
                page.contentHash() + "-duplicate",
                page.links()
        );
    }
}
