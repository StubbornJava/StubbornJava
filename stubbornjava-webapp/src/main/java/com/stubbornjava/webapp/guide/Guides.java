package com.stubbornjava.webapp.guide;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.contentful.java.cda.CDAEntry;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.stubbornjava.common.Templating;
import com.stubbornjava.webapp.ContentfulClient;

public class Guides {
    private Guides() {}
    private static final String contentType = "guide";
    private static final LoadingCache<String, Guide> guideCache = CacheBuilder.newBuilder()
            .maximumSize(250)
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build(CacheLoader.from(Guides::loadGuide));
    private static final LoadingCache<String, List<GuideTitle>> guideTitlesCache = CacheBuilder.newBuilder()
            .maximumSize(1)
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build(CacheLoader.from(() -> Guides.loadTitles()));

    public static List<GuideTitle> findTitles() {
        return guideTitlesCache.getUnchecked("instance");
    }

    private static List<GuideTitle> loadTitles() {
        return ContentfulClient.dsl()
                .withContentType(contentType)
                .select("fields.title", "fields.slug")
                .all()
                .entries()
                .values()
                .stream()
                .map(Guides::guideTitleFromEntry)
                .collect(Collectors.toList());
    }

    public static Guide findBySlug(String slug) {
        if (null == slug) {
            return null;
        }
        return guideCache.getUnchecked(slug);
    }

    private static Guide loadGuide(String slug) {
        Guide guide = ContentfulClient.dsl()
            .withContentType(contentType)
            .where("fields.slug", slug)
            .all()
            .entries()
            .values()
            .stream()
            .findFirst()
            .map(Guides::guideFromEntry).orElse(null);
        return guide;
    }

    private static Guide guideFromEntry(CDAEntry entry) {
        String title = entry.getField("title");
        String slug = entry.getField("slug");
        String bodyMarkdown = entry.getField("body");
        Map<String, String> data = Maps.newHashMapWithExpectedSize(1);
        data.put("data", bodyMarkdown);
        String bodyHtml = Templating.instance().renderRawHtmlTemplate("{{md data}}", data);
        return new Guide(title, slug, bodyHtml);
    }

    private static GuideTitle guideTitleFromEntry(CDAEntry entry) {
        String title = entry.getField("title");
        String slug = entry.getField("slug");
        return new GuideTitle(title, slug);
    }

    public static void main(String[] args) {
        findBySlug("webserver-guide");
    }
}
