package com.stubbornjava.webapp;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import org.jooq.lambda.Seq;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.stubbornjava.common.seo.InMemorySitemap;
import com.stubbornjava.webapp.guide.GuideTitle;
import com.stubbornjava.webapp.guide.Guides;
import com.stubbornjava.webapp.post.JavaLib;
import com.stubbornjava.webapp.post.Posts;
import com.stubbornjava.webapp.post.Tag;
import com.stubbornjava.webapp.post.Tags;

import okhttp3.HttpUrl;

// {{start:sitemapgen}}
class StubbornJavaSitemapGenerator {
    private static final String HOST = "https://www.stubbornjava.com";

    private static final InMemorySitemap sitemap = InMemorySitemap.fromSupplier(StubbornJavaSitemapGenerator::generateSitemap);
    public static InMemorySitemap getSitemap() {
        return sitemap;
    }

    private static Map<String, List<String>> generateSitemap() {
        Map<String, List<String>> index = Maps.newHashMap();
        try {
            index.put("posts", genPosts());
            index.put("guides", genGuides());
            index.put("recommendations", genRecommendations());
            index.put("tags", genTags());
            index.put("libraries", genLibraries());
            return index;
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static List<String> genPosts() throws MalformedURLException {
        WebSitemapGenerator wsg = new WebSitemapGenerator(HOST);
        List<String> slugs = Posts.getAllSlugs();
        for (String slug: slugs) {
            String url = HttpUrl.parse(HOST)
                                .newBuilder()
                                .addPathSegment("posts")
                                .addPathSegment(slug)
                                .build()
                                .toString();
            wsg.addUrl(url);
        }
        return wsg.writeAsStrings();
    }

    private static List<String> genGuides() throws MalformedURLException {
        WebSitemapGenerator wsg = new WebSitemapGenerator(HOST);
        List<GuideTitle> guides = Guides.findTitles();
        for (GuideTitle guide : guides) {
            String url = HttpUrl.parse(HOST)
                                .newBuilder()
                                .addPathSegment("guides")
                                .addPathSegment(guide.getSlug())
                                .build()
                                .toString();
            wsg.addUrl(url);
        }
        return wsg.writeAsStrings();
    }

    private static List<String> genRecommendations() throws MalformedURLException {
        WebSitemapGenerator wsg = new WebSitemapGenerator(HOST);
        List<String> recommendations = Lists.newArrayList(
            "java-libraries"
            , "best-selling-html-css-themes-and-website-templates"
        );
        for (String recommendation : recommendations) {
            String url = HttpUrl.parse(HOST)
                                .newBuilder()
                                .addPathSegment(recommendation)
                                .build()
                                .toString();
            wsg.addUrl(url);
        }
        return wsg.writeAsStrings();
    }

    private static List<String> genTags() throws MalformedURLException {
        WebSitemapGenerator wsg = new WebSitemapGenerator(HOST);
        List<Tag> tags = Tags.getTags();
        for (Tag tag : tags) {
            String url = HttpUrl.parse(HOST)
                                .newBuilder()
                                .addPathSegment("tags")
                                .addPathSegment(tag.getName())
                                .addEncodedPathSegment("posts")
                                .build()
                                .toString();
            wsg.addUrl(url);
        }
        return wsg.writeAsStrings();
    }

    private static List<String> genLibraries() throws MalformedURLException {
        WebSitemapGenerator wsg = new WebSitemapGenerator(HOST);
        List<JavaLib> libraries = Seq.of(JavaLib.values()).toList();
        for (JavaLib lib : libraries) {
            String url = HttpUrl.parse(HOST)
                                .newBuilder()
                                .addPathSegment("java-libraries")
                                .addPathSegment(lib.getName())
                                .build()
                                .toString();
            wsg.addUrl(url);
        }
        return wsg.writeAsStrings();
    }

    public static void main(String[] args) {
        generateSitemap();
    }
}
// {{end:sitemapgen}}

