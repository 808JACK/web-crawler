package com.example.demo.workflow.postprocessor;

import com.example.demo.model.CrawlContext;
import com.example.demo.model.ProcessedPage;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(10)
public class CleanTextPostProcessor implements ContentPostProcessor {

    @Override
    public ProcessedPage process(ProcessedPage page, CrawlContext context) {
        return new ProcessedPage(
                page.url(),
                page.depth(),
                page.title(),
                page.text().replaceAll("\\s+", " ").trim(),
                page.contentHash(),
                page.links()
        );
    }
}
