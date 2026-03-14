package com.example.demo.service;

import com.example.demo.model.DownloadResult;

public interface PageDownloader {
    DownloadResult download(String url);
}
