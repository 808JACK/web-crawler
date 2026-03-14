package com.example.demo.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

@Entity
@Table(name = "crawl_pages", indexes = {
        @Index(name = "idx_crawl_pages_url", columnList = "normalized_url", unique = true),
        @Index(name = "idx_crawl_pages_hash", columnList = "content_hash"),
        @Index(name = "idx_crawl_pages_domain", columnList = "domain_name")
})
public class CrawlPageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "normalized_url", nullable = false, unique = true, length = 2000)
    private String normalizedUrl;

    @Column(name = "domain_name", nullable = false, length = 255)
    private String domainName;

    @Column(name = "title")
    private String title;

    @Column(name = "content_hash", nullable = false, length = 128)
    private String contentHash;

    @Column(name = "depth_level", nullable = false)
    private int depthLevel;

    @Column(name = "duplicate_content", nullable = false)
    private boolean duplicateContent;

    @Column(name = "crawled_at", nullable = false)
    private OffsetDateTime crawledAt;

    public Long getId() {
        return id;
    }

    public String getNormalizedUrl() {
        return normalizedUrl;
    }

    public void setNormalizedUrl(String normalizedUrl) {
        this.normalizedUrl = normalizedUrl;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentHash() {
        return contentHash;
    }

    public void setContentHash(String contentHash) {
        this.contentHash = contentHash;
    }

    public int getDepthLevel() {
        return depthLevel;
    }

    public void setDepthLevel(int depthLevel) {
        this.depthLevel = depthLevel;
    }

    public boolean isDuplicateContent() {
        return duplicateContent;
    }

    public void setDuplicateContent(boolean duplicateContent) {
        this.duplicateContent = duplicateContent;
    }

    public OffsetDateTime getCrawledAt() {
        return crawledAt;
    }

    public void setCrawledAt(OffsetDateTime crawledAt) {
        this.crawledAt = crawledAt;
    }
}
