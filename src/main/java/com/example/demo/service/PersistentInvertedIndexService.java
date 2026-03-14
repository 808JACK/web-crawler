package com.example.demo.service;

import com.example.demo.dto.SearchResponse;
import com.example.demo.dto.SearchResult;
import com.example.demo.model.ProcessedPage;
import com.example.demo.persistence.InvertedIndexEntryEntity;
import com.example.demo.persistence.InvertedIndexEntryRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PersistentInvertedIndexService {

    private final InvertedIndexEntryRepository invertedIndexEntryRepository;

    public PersistentInvertedIndexService(InvertedIndexEntryRepository invertedIndexEntryRepository) {
        this.invertedIndexEntryRepository = invertedIndexEntryRepository;
    }

    public void index(ProcessedPage page) {
        invertedIndexEntryRepository.deleteByNormalizedUrl(page.url());
        List<InvertedIndexEntryEntity> entries = tokenize(page.text()).stream()
                .distinct()
                .map(token -> {
                    InvertedIndexEntryEntity entity = new InvertedIndexEntryEntity();
                    entity.setTokenValue(token);
                    entity.setNormalizedUrl(page.url());
                    entity.setPageTitle(page.title());
                    return entity;
                })
                .toList();
        invertedIndexEntryRepository.saveAll(entries);
    }

    public SearchResponse search(String query) {
        List<String> tokens = tokenize(query);
        if (tokens.isEmpty()) {
            return new SearchResponse(query, List.of());
        }

        Map<String, SearchAccumulator> grouped = new LinkedHashMap<>();
        for (InvertedIndexEntryEntity entry : invertedIndexEntryRepository.findByTokenValueIn(tokens)) {
            grouped.compute(entry.getNormalizedUrl(), (url, current) -> {
                SearchAccumulator next = current == null
                        ? new SearchAccumulator(entry.getNormalizedUrl(), entry.getPageTitle())
                        : current;
                next.increment();
                return next;
            });
        }

        List<SearchResult> results = grouped.values().stream()
                .sorted(Comparator.comparingLong(SearchAccumulator::matchedTerms).reversed())
                .map(accumulator -> new SearchResult(accumulator.url, accumulator.title, accumulator.matchedTerms()))
                .toList();
        return new SearchResponse(query, results);
    }

    private List<String> tokenize(String value) {
        return Arrays.stream(value == null ? new String[0] : value.toLowerCase().split("\\W+"))
                .filter(token -> token.length() > 2)
                .toList();
    }

    private static final class SearchAccumulator {
        private final String url;
        private final String title;
        private long matchedTerms;

        private SearchAccumulator(String url, String title) {
            this.url = url;
            this.title = title;
        }

        private void increment() {
            matchedTerms++;
        }

        private long matchedTerms() {
            return matchedTerms;
        }
    }
}
