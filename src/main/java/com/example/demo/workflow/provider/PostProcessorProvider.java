package com.example.demo.workflow.provider;

import com.example.demo.workflow.WorkflowExecutor;
import com.example.demo.workflow.postprocessor.WorkflowPostProcessor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostProcessorProvider {

    private final List<WorkflowPostProcessor<?>> processors;

    public PostProcessorProvider(List<WorkflowPostProcessor<?>> processors) {
        this.processors = processors;
    }

    @SuppressWarnings("unchecked")
    public <T> List<WorkflowPostProcessor<T>> provide(WorkflowExecutor.Action action) {
        return processors.stream()
                .filter(processor -> processor.supports(action))
                .map(processor -> (WorkflowPostProcessor<T>) processor)
                .toList();
    }
}
