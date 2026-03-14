package com.example.demo.service;

import com.example.demo.model.DownloadResult;
import com.example.demo.model.ParsedPage;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PageParser {

    public ParsedPage parse(DownloadResult downloadResult, int depth) {
        List<String> links = downloadResult.document()
                .select("a[href]")
                .stream()
                .map(element -> element.absUrl("href"))
                .filter(link -> !link.isBlank())
                .distinct()
                .toList();

        String text = downloadResult.document().body() == null ? "" : downloadResult.document().body().text();

        return new ParsedPage(
                downloadResult.url(),
                depth,
                downloadResult.document().title(),
                text,
                links
        );
    }
}
