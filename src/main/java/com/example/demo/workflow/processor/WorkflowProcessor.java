package com.example.demo.workflow.processor;

import com.example.demo.workflow.WorkflowExecutor;

public interface WorkflowProcessor {

    boolean supports(WorkflowExecutor.Action action);

    <R, T> WorkflowExecutor.Response<R, T> execute(WorkflowExecutor.Step step);
}
