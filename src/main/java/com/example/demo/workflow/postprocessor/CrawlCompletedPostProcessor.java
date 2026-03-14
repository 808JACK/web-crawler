package com.example.demo.workflow.postprocessor;

import com.example.demo.dto.CrawlResponse;
import com.example.demo.workflow.WorkflowExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CrawlCompletedPostProcessor implements WorkflowPostProcessor<CrawlResponse> {

    @Override
    public boolean supports(WorkflowExecutor.Action action) {
        return action == WorkflowExecutor.Action.CRAWL;
    }

    @Override
    public void process(CrawlResponse payload) {
        log.info("crawl completed processedPages={}", payload.processedPages());
    }
}
