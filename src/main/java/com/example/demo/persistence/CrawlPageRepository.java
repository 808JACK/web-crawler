package com.example.demo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CrawlPageRepository extends JpaRepository<CrawlPageEntity, Long> {

    boolean existsByNormalizedUrl(String normalizedUrl);

    boolean existsByContentHash(String contentHash);
}
