package com.example.demo.workflow.postprocessor;

import com.example.demo.model.CrawlContext;
import com.example.demo.model.ProcessedPage;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
@Order(20)
public class ContentHashPostProcessor implements ContentPostProcessor {

    @Override
    public ProcessedPage process(ProcessedPage page, CrawlContext context) {
        return new ProcessedPage(
                page.url(),
                page.depth(),
                page.title(),
                page.text(),
                sha256(page.text()),
                page.links()
        );
    }

    private String sha256(String value) {
        try {
            byte[] bytes = MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte current : bytes) {
                builder.append(String.format("%02x", current));
            }
            return builder.toString();
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to hash content", exception);
        }
    }
}
