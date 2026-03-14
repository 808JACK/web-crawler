package com.example.demo.controller;

import com.example.demo.dto.CrawlRequest;
import com.example.demo.dto.CrawlResponse;
import com.example.demo.service.CrawlWorkflowService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/crawler")
public class CrawlController {

    private final CrawlWorkflowService crawlWorkflowService;

    public CrawlController(CrawlWorkflowService crawlWorkflowService) {
        this.crawlWorkflowService = crawlWorkflowService;
    }

    @PostMapping("/run")
    public CrawlResponse run(@Valid @RequestBody CrawlRequest request) {
        return crawlWorkflowService.run(request);
    }
}
