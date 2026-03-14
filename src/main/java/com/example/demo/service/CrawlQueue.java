package com.example.demo.service;

import com.example.demo.model.CrawlTask;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Optional;
import java.util.Queue;

public class CrawlQueue {

    private final Queue<CrawlTask> tasks = new ArrayDeque<>();

    public void reset(Collection<CrawlTask> seedTasks) {
        tasks.clear();
        tasks.addAll(seedTasks);
    }

    public void add(CrawlTask task) {
        tasks.offer(task);
    }

    public Optional<CrawlTask> poll() {
        return Optional.ofNullable(tasks.poll());
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }
}
