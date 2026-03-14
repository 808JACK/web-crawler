package com.example.demo.workflow.provider;

import com.example.demo.workflow.WorkflowExecutor;
import com.example.demo.workflow.processor.WorkflowProcessor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcessorProvider {

    private final List<WorkflowProcessor> processors;

    public ProcessorProvider(List<WorkflowProcessor> processors) {
        this.processors = processors;
    }

    public WorkflowProcessor provide(WorkflowExecutor.Action action) {
        return processors.stream()
                .filter(processor -> processor.supports(action))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No workflow processor registered for " + action));
    }
}
