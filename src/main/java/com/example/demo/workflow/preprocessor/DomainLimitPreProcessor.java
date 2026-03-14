package com.example.demo.workflow.preprocessor;

import com.example.demo.model.CrawlContext;
import com.example.demo.model.UrlProcessingResult;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@Order(45)
public class DomainLimitPreProcessor implements UrlPreProcessor {

    @Override
    public UrlProcessingResult process(UrlProcessingResult current, CrawlContext context) {
        String domain = URI.create(current.task().url()).getHost();
        if (!context.canVisitDomain(domain)) {
            return UrlProcessingResult.blocked(current.task(), "Per-domain page limit exceeded");
        }
        return current;
    }
}
