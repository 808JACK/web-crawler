package com.example.demo.workflow.postprocessor;

import com.example.demo.workflow.WorkflowExecutor;

public interface WorkflowPostProcessor<T> {

    boolean supports(WorkflowExecutor.Action action);

    void process(T payload);
}
