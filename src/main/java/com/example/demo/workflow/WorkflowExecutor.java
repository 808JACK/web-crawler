package com.example.demo.workflow;

import com.example.demo.dto.CrawlRequest;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

public interface WorkflowExecutor {

    <R, T> R execute(Step step);

    enum Action {
        CRAWL
    }

    @Builder
    @Data
    class Response<R, T> {
        private R response;
        private T postProcessorPayload;
    }

    @Getter
    @ToString
    class Step {
        private final Action action;

        public Step(Action action) {
            this.action = action;
        }
    }

    @Getter
    @ToString(callSuper = true)
    class CrawlStep extends Step {
        private final CrawlRequest request;

        public CrawlStep(Action action, CrawlRequest request) {
            super(action);
            this.request = request;
        }
    }
}
