package com.example.demo.model;

import org.jsoup.nodes.Document;

public record DownloadResult(String url, Document document) {
}
