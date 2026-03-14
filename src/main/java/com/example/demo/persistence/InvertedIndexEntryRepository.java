package com.example.demo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface InvertedIndexEntryRepository extends JpaRepository<InvertedIndexEntryEntity, Long> {

    void deleteByNormalizedUrl(String normalizedUrl);

    List<InvertedIndexEntryEntity> findByTokenValueIn(Collection<String> tokenValues);
}
