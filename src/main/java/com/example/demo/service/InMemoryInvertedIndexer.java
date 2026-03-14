package com.example.demo.service;

import com.example.demo.model.ProcessedPage;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryInvertedIndexer {

    private final Map<String, Set<String>> index = new ConcurrentHashMap<>();

    public void clear() {
        index.clear();
    }

    public void index(ProcessedPage page) {
        Arrays.stream(page.text().toLowerCase().split("\\W+"))
                .filter(token -> token.length() > 2)
                .forEach(token -> index.computeIfAbsent(token, ignored -> ConcurrentHashMap.newKeySet()).add(page.url()));
    }

    public Map<String, List<String>> snapshot(int limit) {
        return index.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .limit(limit)
                .collect(LinkedHashMap::new,
                        (map, entry) -> map.put(entry.getKey(), entry.getValue().stream().sorted().toList()),
                        LinkedHashMap::putAll);
    }
}
