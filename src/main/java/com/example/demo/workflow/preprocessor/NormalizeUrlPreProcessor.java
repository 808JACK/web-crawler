package com.example.demo.workflow.preprocessor;

import com.example.demo.model.CrawlContext;
import com.example.demo.model.CrawlTask;
import com.example.demo.model.UrlProcessingResult;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@Order(10)
public class NormalizeUrlPreProcessor implements UrlPreProcessor {

    @Override
    public UrlProcessingResult process(UrlProcessingResult current, CrawlContext context) {
        try {
            URI uri = URI.create(current.task().url().trim()).normalize();
            String path = uri.getPath() == null || uri.getPath().isBlank() ? "/" : uri.getPath();
            URI normalized = new URI(
                    uri.getScheme() == null ? "https" : uri.getScheme().toLowerCase(),
                    uri.getUserInfo(),
                    uri.getHost() == null ? null : uri.getHost().toLowerCase(),
                    uri.getPort(),
                    path,
                    uri.getQuery(),
                    null
            );
            return UrlProcessingResult.allowed(new CrawlTask(normalized.toString(), current.task().depth()));
        } catch (Exception exception) {
            return UrlProcessingResult.blocked(current.task(), "Invalid URL");
        }
    }
}
