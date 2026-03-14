package com.example.demo.workflow.processor;

import com.example.demo.dto.CrawlResponse;
import com.example.demo.service.CrawlOrchestrator;
import com.example.demo.workflow.WorkflowExecutor;
import org.springframework.stereotype.Component;

@Component
public class CrawlWorkflowProcessor implements WorkflowProcessor {

    private final CrawlOrchestrator crawlOrchestrator;

    public CrawlWorkflowProcessor(CrawlOrchestrator crawlOrchestrator) {
        this.crawlOrchestrator = crawlOrchestrator;
    }

    @Override
    public boolean supports(WorkflowExecutor.Action action) {
        return action == WorkflowExecutor.Action.CRAWL;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R, T> WorkflowExecutor.Response<R, T> execute(WorkflowExecutor.Step step) {
        WorkflowExecutor.CrawlStep crawlStep = (WorkflowExecutor.CrawlStep) step;
        CrawlResponse response = crawlOrchestrator.crawl(crawlStep.getRequest());
        return (WorkflowExecutor.Response<R, T>) WorkflowExecutor.Response.builder()
                .response(response)
                .postProcessorPayload(response)
                .build();
    }
}
