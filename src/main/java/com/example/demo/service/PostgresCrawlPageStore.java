package com.example.demo.service;

import com.example.demo.model.ProcessedPage;
import com.example.demo.persistence.CrawlPageEntity;
import com.example.demo.persistence.CrawlPageRepository;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.OffsetDateTime;

@Component
public class PostgresCrawlPageStore implements CrawlPageStore {

    private final CrawlPageRepository crawlPageRepository;

    public PostgresCrawlPageStore(CrawlPageRepository crawlPageRepository) {
        this.crawlPageRepository = crawlPageRepository;
    }

    @Override
    public boolean existsByUrl(String url) {
        return crawlPageRepository.existsByNormalizedUrl(url);
    }

    @Override
    public boolean existsByContentHash(String contentHash) {
        return crawlPageRepository.existsByContentHash(contentHash);
    }

    @Override
    public void save(ProcessedPage page) {
        if (crawlPageRepository.existsByNormalizedUrl(page.url())) {
            return;
        }

        CrawlPageEntity entity = new CrawlPageEntity();
        entity.setNormalizedUrl(page.url());
        entity.setDomainName(URI.create(page.url()).getHost());
        entity.setTitle(page.title());
        entity.setContentHash(page.contentHash().replace("-duplicate", ""));
        entity.setDepthLevel(page.depth());
        entity.setDuplicateContent(page.contentHash().endsWith("-duplicate"));
        entity.setCrawledAt(OffsetDateTime.now());
        crawlPageRepository.save(entity);
    }
}
