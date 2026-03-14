package com.example.demo.workflow;

import com.example.demo.dto.CrawlRequest;
import com.example.demo.dto.CrawlResponse;
import com.example.demo.workflow.postprocessor.CrawlCompletedPostProcessor;
import com.example.demo.workflow.preprocessor.CrawlRequestValidationPreProcessor;
import com.example.demo.workflow.processor.WorkflowProcessor;
import com.example.demo.workflow.provider.PostProcessorProvider;
import com.example.demo.workflow.provider.PreProcessorProvider;
import com.example.demo.workflow.provider.ProcessorProvider;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultWorkflowExecutorTest {

    @Test
    void executesCrawlWorkflow() {
        DefaultWorkflowExecutor executor = new DefaultWorkflowExecutor(
                new PreProcessorProvider(List.of(new CrawlRequestValidationPreProcessor())),
                new ProcessorProvider(List.of(new WorkflowProcessor() {
                    @Override
                    public boolean supports(WorkflowExecutor.Action action) {
                        return action == WorkflowExecutor.Action.CRAWL;
                    }

                    @Override
                    @SuppressWarnings("unchecked")
                    public <R, T> WorkflowExecutor.Response<R, T> execute(WorkflowExecutor.Step step) {
                        CrawlResponse response = new CrawlResponse(
                                1,
                                List.of(),
                                Map.of("crawler", List.of("https://example.com"))
                        );
                        return (WorkflowExecutor.Response<R, T>) WorkflowExecutor.Response.builder()
                                .response(response)
                                .postProcessorPayload(response)
                                .build();
                    }
                })),
                new PostProcessorProvider(List.of(new CrawlCompletedPostProcessor()))
        );

        CrawlResponse response = executor.execute(new WorkflowExecutor.CrawlStep(
                WorkflowExecutor.Action.CRAWL,
                new CrawlRequest(List.of("https://example.com"), 1, 1, 1, 2)
        ));

        assertEquals(1, response.processedPages());
    }
}
