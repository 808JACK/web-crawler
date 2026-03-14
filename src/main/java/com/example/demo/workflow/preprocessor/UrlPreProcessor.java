package com.example.demo.workflow.preprocessor;

import com.example.demo.model.CrawlContext;
import com.example.demo.model.UrlProcessingResult;

public interface UrlPreProcessor {

    UrlProcessingResult process(UrlProcessingResult current, CrawlContext context);
}
