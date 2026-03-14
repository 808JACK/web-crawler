package com.example.demo.workflow.preprocessor;

import com.example.demo.model.CrawlContext;
import com.example.demo.model.UrlProcessingResult;
import com.example.demo.service.RobotsTxtService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(30)
public class RobotsValidationPreProcessor implements UrlPreProcessor {

    private final RobotsTxtService robotsTxtService;

    public RobotsValidationPreProcessor(RobotsTxtService robotsTxtService) {
        this.robotsTxtService = robotsTxtService;
    }

    @Override
    public UrlProcessingResult process(UrlProcessingResult current, CrawlContext context) {
        String url = current.task().url();
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return UrlProcessingResult.blocked(current.task(), "Only HTTP(S) URLs are supported");
        }
        if (!robotsTxtService.isAllowed(url)) {
            return UrlProcessingResult.blocked(current.task(), "Blocked by robots.txt");
        }
        return current;
    }
}
