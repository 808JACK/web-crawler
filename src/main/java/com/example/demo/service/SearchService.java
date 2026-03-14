package com.example.demo.service;

import com.example.demo.dto.SearchResponse;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

    private final PersistentInvertedIndexService persistentInvertedIndexService;

    public SearchService(PersistentInvertedIndexService persistentInvertedIndexService) {
        this.persistentInvertedIndexService = persistentInvertedIndexService;
    }

    public SearchResponse search(String query) {
        return persistentInvertedIndexService.search(query);
    }
}
