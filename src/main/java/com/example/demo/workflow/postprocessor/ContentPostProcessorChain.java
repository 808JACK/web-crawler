package com.example.demo.workflow.postprocessor;

import com.example.demo.model.CrawlContext;
import com.example.demo.model.ParsedPage;
import com.example.demo.model.ProcessedPage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContentPostProcessorChain {

    private final List<ContentPostProcessor> processors;

    public ContentPostProcessorChain(List<ContentPostProcessor> processors) {
        this.processors = processors;
    }

    public ProcessedPage process(ParsedPage parsedPage, CrawlContext context) {
        ProcessedPage current = new ProcessedPage(
                parsedPage.url(),
                parsedPage.depth(),
                parsedPage.title(),
                parsedPage.text(),
                "",
                parsedPage.links()
        );

        for (ContentPostProcessor processor : processors) {
            current = processor.process(current, context);
        }
        return current;
    }
}
