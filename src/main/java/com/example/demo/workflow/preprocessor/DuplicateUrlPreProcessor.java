package com.example.demo.workflow.preprocessor;

import com.example.demo.model.CrawlContext;
import com.example.demo.model.UrlProcessingResult;
import com.example.demo.service.CrawlPageStore;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(20)
public class DuplicateUrlPreProcessor implements UrlPreProcessor {

    private final CrawlPageStore crawlPageStore;

    public DuplicateUrlPreProcessor(CrawlPageStore crawlPageStore) {
        this.crawlPageStore = crawlPageStore;
    }

    @Override
    public UrlProcessingResult process(UrlProcessingResult current, CrawlContext context) {
        if (!context.markUrl(current.task().url()) || crawlPageStore.existsByUrl(current.task().url())) {
            return UrlProcessingResult.blocked(current.task(), "Duplicate URL");
        }
        return current;
    }
}
