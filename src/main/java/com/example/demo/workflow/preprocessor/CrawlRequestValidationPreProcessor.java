package com.example.demo.workflow.preprocessor;

import com.example.demo.workflow.WorkflowExecutor;
import org.springframework.stereotype.Component;

@Component
public class CrawlRequestValidationPreProcessor implements WorkflowPreProcessor<WorkflowExecutor.CrawlStep> {

    @Override
    public boolean supports(WorkflowExecutor.Action action) {
        return action == WorkflowExecutor.Action.CRAWL;
    }

    @Override
    public void process(WorkflowExecutor.CrawlStep step) {
        if (step.getRequest() == null || step.getRequest().seedUrls() == null || step.getRequest().seedUrls().isEmpty()) {
            throw new IllegalArgumentException("Seed URLs are required");
        }
    }
}
