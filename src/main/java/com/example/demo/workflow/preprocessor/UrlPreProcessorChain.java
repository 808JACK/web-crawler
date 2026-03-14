package com.example.demo.workflow.preprocessor;

import com.example.demo.model.CrawlContext;
import com.example.demo.model.CrawlTask;
import com.example.demo.model.UrlProcessingResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UrlPreProcessorChain {

    private final List<UrlPreProcessor> processors;

    public UrlPreProcessorChain(List<UrlPreProcessor> processors) {
        this.processors = processors;
    }

    public UrlProcessingResult process(CrawlTask task, CrawlContext context) {
        UrlProcessingResult current = UrlProcessingResult.allowed(task);
        for (UrlPreProcessor processor : processors) {
            current = processor.process(current, context);
            if (!current.allowed()) {
                return current;
            }
        }
        return current;
    }
}
