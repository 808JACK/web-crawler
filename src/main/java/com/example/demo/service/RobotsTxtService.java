package com.example.demo.service;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RobotsTxtService {

    private final Map<String, RobotsRules> cache = new ConcurrentHashMap<>();

    public boolean isAllowed(String url) {
        URI uri = URI.create(url);
        String domainKey = uri.getScheme() + "://" + uri.getHost() + (uri.getPort() > 0 ? ":" + uri.getPort() : "");
        RobotsRules rules = cache.computeIfAbsent(domainKey, this::fetchRules);
        String path = uri.getPath() == null || uri.getPath().isBlank() ? "/" : uri.getPath();
        return rules.isAllowed(path);
    }

    private RobotsRules fetchRules(String domainKey) {
        try {
            Connection.Response response = Jsoup.connect(domainKey + "/robots.txt")
                    .ignoreContentType(true)
                    .timeout(3_000)
                    .execute();
            return parse(response.body());
        } catch (IOException exception) {
            return RobotsRules.allowAll();
        }
    }

    private RobotsRules parse(String body) {
        List<String> disallowedPaths = new ArrayList<>();
        boolean appliesToAllBots = false;

        for (String rawLine : body.split("\\R")) {
            String line = rawLine.strip();
            if (line.isBlank() || line.startsWith("#")) {
                continue;
            }

            String lowerLine = line.toLowerCase();
            if (lowerLine.startsWith("user-agent:")) {
                appliesToAllBots = "*".equals(line.substring("user-agent:".length()).trim());
            } else if (appliesToAllBots && lowerLine.startsWith("disallow:")) {
                String path = line.substring("disallow:".length()).trim();
                if (!path.isBlank()) {
                    disallowedPaths.add(path);
                }
            }
        }

        return new RobotsRules(disallowedPaths);
    }

    private record RobotsRules(List<String> disallowedPaths) {
        static RobotsRules allowAll() {
            return new RobotsRules(List.of());
        }

        boolean isAllowed(String path) {
            return disallowedPaths.stream().noneMatch(path::startsWith);
        }
    }
}
