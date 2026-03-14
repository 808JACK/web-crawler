package com.example.demo.workflow.postprocessor;

import com.example.demo.model.CrawlContext;
import com.example.demo.model.ProcessedPage;

public interface ContentPostProcessor {

    ProcessedPage process(ProcessedPage page, CrawlContext context);
}
