package com.example.demo.workflow;

import com.example.demo.workflow.postprocessor.WorkflowPostProcessor;
import com.example.demo.workflow.preprocessor.WorkflowPreProcessor;
import com.example.demo.workflow.processor.WorkflowProcessor;
import com.example.demo.workflow.provider.PostProcessorProvider;
import com.example.demo.workflow.provider.PreProcessorProvider;
import com.example.demo.workflow.provider.ProcessorProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultWorkflowExecutor implements WorkflowExecutor {

    private final PreProcessorProvider preProcessorProvider;
    private final ProcessorProvider processorProvider;
    private final PostProcessorProvider postProcessorProvider;

    public DefaultWorkflowExecutor(
            PreProcessorProvider preProcessorProvider,
            ProcessorProvider processorProvider,
            PostProcessorProvider postProcessorProvider
    ) {
        this.preProcessorProvider = preProcessorProvider;
        this.processorProvider = processorProvider;
        this.postProcessorProvider = postProcessorProvider;
    }

    @Override
    public <R, T> R execute(Step step) {
        List<WorkflowPreProcessor<Step>> preProcessors = preProcessorProvider.provide(step.getAction());
        preProcessors.forEach(preProcessor -> preProcessor.process(step));

        WorkflowProcessor processor = processorProvider.provide(step.getAction());
        Response<R, T> response = processor.execute(step);

        List<WorkflowPostProcessor<T>> postProcessors = postProcessorProvider.provide(step.getAction());
        postProcessors.forEach(postProcessor -> postProcessor.process(response.getPostProcessorPayload()));

        return response.getResponse();
    }
}
