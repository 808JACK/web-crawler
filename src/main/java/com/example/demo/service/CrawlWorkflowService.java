package com.example.demo.service;

import com.example.demo.dto.CrawlRequest;
import com.example.demo.dto.CrawlResponse;
import com.example.demo.workflow.WorkflowExecutor;
import org.springframework.stereotype.Service;

@Service
public class CrawlWorkflowService {

    private final WorkflowExecutor workflowExecutor;

    public CrawlWorkflowService(WorkflowExecutor workflowExecutor) {
        this.workflowExecutor = workflowExecutor;
    }

    public CrawlResponse run(CrawlRequest request) {
        return workflowExecutor.execute(new WorkflowExecutor.CrawlStep(WorkflowExecutor.Action.CRAWL, request));
    }
}
