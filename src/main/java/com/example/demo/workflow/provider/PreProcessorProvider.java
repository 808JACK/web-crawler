package com.example.demo.workflow.provider;

import com.example.demo.workflow.WorkflowExecutor;
import com.example.demo.workflow.preprocessor.WorkflowPreProcessor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PreProcessorProvider {

    private final List<WorkflowPreProcessor<?>> processors;

    public PreProcessorProvider(List<WorkflowPreProcessor<?>> processors) {
        this.processors = processors;
    }

    @SuppressWarnings("unchecked")
    public <S extends WorkflowExecutor.Step> List<WorkflowPreProcessor<S>> provide(WorkflowExecutor.Action action) {
        return processors.stream()
                .filter(processor -> processor.supports(action))
                .map(processor -> (WorkflowPreProcessor<S>) processor)
                .toList();
    }
}
