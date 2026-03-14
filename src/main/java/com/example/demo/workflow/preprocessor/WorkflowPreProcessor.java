package com.example.demo.workflow.preprocessor;

import com.example.demo.workflow.WorkflowExecutor;

public interface WorkflowPreProcessor<S extends WorkflowExecutor.Step> {

    boolean supports(WorkflowExecutor.Action action);

    void process(S step);
}
