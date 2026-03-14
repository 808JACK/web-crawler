package com.example.demo.service;

import com.example.demo.model.DownloadResult;
import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class JsoupPageDownloader implements PageDownloader {

    private static final int REQUEST_TIMEOUT_MILLIS = (int) TimeUnit.SECONDS.toMillis(5);
    private static final int MAX_RETRIES = 2;
    private static final int MAX_REDIRECTS = 3;

    @Override
    public DownloadResult download(String url) {
        IOException lastException = null;

        for (int attempt = 0; attempt <= MAX_RETRIES; attempt++) {
            try {
                return fetchWithRedirectLimit(url, MAX_REDIRECTS);
            } catch (IOException exception) {
                lastException = exception;
            }
        }

        throw new IllegalStateException("Unable to download " + url, lastException);
    }

    private DownloadResult fetchWithRedirectLimit(String url, int redirectsRemaining) throws IOException {
        Connection.Response response = Jsoup.connect(url)
                .userAgent("demo-webcrawler/1.0")
                .timeout(REQUEST_TIMEOUT_MILLIS)
                .followRedirects(false)
                .execute();

        int statusCode = response.statusCode();
        if (statusCode >= 300 && statusCode < 400) {
            if (redirectsRemaining == 0) {
                throw new IOException("Redirect limit exceeded for " + url);
            }
            String location = response.header("Location");
            if (location == null || location.isBlank()) {
                throw new IOException("Redirect response missing Location header for " + url);
            }
            try {
                return fetchWithRedirectLimit(response.url().toURI().resolve(location).toString(), redirectsRemaining - 1);
            } catch (Exception exception) {
                throw new IOException("Unable to resolve redirect for " + url, exception);
            }
        }

        Document document = response.parse();
        return new DownloadResult(response.url().toString(), document);
    }
}
