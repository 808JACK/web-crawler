package com.example.demo.service;

import com.example.demo.model.CrawlContext;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class DomainRequestLimiter {

    public void await(String url, CrawlContext context) {
        String domain = URI.create(url).getHost();
        long now = System.currentTimeMillis();
        long waitMillis = context.requestIntervalMillis() - (now - context.lastAccessAt(domain));
        if (waitMillis > 0) {
            try {
                Thread.sleep(waitMillis);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Crawler rate limiting interrupted", exception);
            }
        }
        context.updateLastAccessAt(domain, System.currentTimeMillis());
    }
}
