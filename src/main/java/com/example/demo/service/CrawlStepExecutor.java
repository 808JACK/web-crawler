package com.example.demo.service;

import com.example.demo.model.CrawlContext;
import com.example.demo.model.CrawlTask;
import com.example.demo.model.DownloadResult;
import com.example.demo.model.ParsedPage;
import com.example.demo.model.ProcessedPage;
import com.example.demo.workflow.postprocessor.ContentPostProcessorChain;
import com.example.demo.workflow.preprocessor.UrlPreProcessorChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class CrawlStepExecutor {

    private final UrlPreProcessorChain urlPreProcessorChain;
    private final PageDownloader pageDownloader;
    private final PageParser pageParser;
    private final ContentPostProcessorChain contentPostProcessorChain;
    private final PersistentInvertedIndexService persistentInvertedIndexService;
    private final DomainRequestLimiter domainRequestLimiter;
    private final CrawlPageStore crawlPageStore;

    public CrawlStepExecutor(
            UrlPreProcessorChain urlPreProcessorChain,
            PageDownloader pageDownloader,
            PageParser pageParser,
            ContentPostProcessorChain contentPostProcessorChain,
            PersistentInvertedIndexService persistentInvertedIndexService,
            DomainRequestLimiter domainRequestLimiter,
            CrawlPageStore crawlPageStore
    ) {
        this.urlPreProcessorChain = urlPreProcessorChain;
        this.pageDownloader = pageDownloader;
        this.pageParser = pageParser;
        this.contentPostProcessorChain = contentPostProcessorChain;
        this.persistentInvertedIndexService = persistentInvertedIndexService;
        this.domainRequestLimiter = domainRequestLimiter;
        this.crawlPageStore = crawlPageStore;
    }

    public Optional<CrawlStepResult> execute(CrawlTask task, CrawlContext context) {
        var preProcessed = urlPreProcessorChain.process(task, context);
        if (!preProcessed.allowed() || !context.canProcessMore()) {
            return Optional.empty();
        }

        try {
            domainRequestLimiter.await(preProcessed.task().url(), context);
            DownloadResult downloadResult = pageDownloader.download(preProcessed.task().url());
            if (isRedirectDuplicate(preProcessed.task().url(), downloadResult.url(), context)) {
                return Optional.empty();
            }

            ParsedPage parsedPage = pageParser.parse(downloadResult, preProcessed.task().depth());
            ProcessedPage processedPage = contentPostProcessorChain.process(parsedPage, context);

            context.markDomainVisit(URI.create(processedPage.url()).getHost());
            context.getInvertedIndexer().index(processedPage);
            persistentInvertedIndexService.index(processedPage);
            crawlPageStore.save(processedPage);
            context.incrementProcessedPages();

            List<CrawlTask> nextTasks = processedPage.links().stream()
                    .map(link -> new CrawlTask(link, processedPage.depth() + 1))
                    .toList();

            return Optional.of(new CrawlStepResult(processedPage, nextTasks));
        } catch (Exception exception) {
            log.warn("skipping failed crawl url={}", preProcessed.task().url(), exception);
            return Optional.empty();
        }
    }

    private boolean isRedirectDuplicate(String requestedUrl, String resolvedUrl, CrawlContext context) {
        if (requestedUrl.equals(resolvedUrl)) {
            return false;
        }
        return !context.markResolvedUrl(resolvedUrl) || crawlPageStore.existsByUrl(resolvedUrl);
    }

    public record CrawlStepResult(ProcessedPage page, List<CrawlTask> nextTasks) {
    }
}
