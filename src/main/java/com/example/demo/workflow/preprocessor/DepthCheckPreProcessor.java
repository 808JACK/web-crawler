package com.example.demo.workflow.preprocessor;

import com.example.demo.model.CrawlContext;
import com.example.demo.model.UrlProcessingResult;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(40)
public class DepthCheckPreProcessor implements UrlPreProcessor {

    @Override
    public UrlProcessingResult process(UrlProcessingResult current, CrawlContext context) {
        return current.task().depth() <= context.getMaxDepth()
                ? current
                : UrlProcessingResult.blocked(current.task(), "Depth limit exceeded");
    }
}
