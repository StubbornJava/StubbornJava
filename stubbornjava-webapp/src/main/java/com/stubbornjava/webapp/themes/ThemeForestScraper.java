package com.stubbornjava.webapp.themes;

import java.util.List;

import org.jooq.lambda.Seq;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.stubbornjava.common.HttpClient;
import com.stubbornjava.common.Json;
import com.stubbornjava.common.Retry;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ThemeForestScraper {
    private static final Logger log = LoggerFactory.getLogger(ThemeForestScraper.class);
    private static final String affilaiteCode = "StubbornJava";
    private static final String HOST = "https://themeforest.net";
    private static final String POPULAR_THEMES_URL =
        "https://themeforest.net/category/site-templates?view=list&sort=sales";
    // They have an issue with the SSL cert that Java can't recognize automatically.
    // Too lazy to set up in the trusted key store.
    private static final OkHttpClient client = HttpClient.trustAllSslClient(HttpClient.globalClient());

    public static List<HtmlCssTheme> popularThemes() {
        HttpUrl url = HttpUrl.parse(POPULAR_THEMES_URL);
        Request request = new Request.Builder().url(url).get().build();
        String html = Retry.retryUntilSuccessfulWithBackoff(
            () -> client.newCall(request).execute()
        );

        Elements elements = Jsoup.parse(html).select("script");
        Element script = Seq.seq(elements)
                            .filter(e -> {

                                return e.html().startsWith("window.INITIAL_STATE=");
                            })
                            .findFirst().orElse(null);
        String rawJson = script.html().substring("window.INITIAL_STATE=".length());
        JsonNode node = Json.serializer().nodeFromJson(rawJson);
        return Seq.seq(node.path("searchPage").path("results").path("matches"))
                  .map(ThemeForestScraper::themeFromElement)
                  .toList();

                                        //.map(ThemeForestScraper::themeFromElement).toList();

    }

    private static HtmlCssTheme themeFromElement(JsonNode node) {
        String title = node.path("name").textValue();
        String url = HttpUrl.parse(node.path("url").asText())
                            .newBuilder()
                            .addQueryParameter("ref", affilaiteCode)
                            .build().toString();
        String imageUrl = node.path("previews").path("landscape_preview").path("landscape_url").asText();
        int downloads = node.path("number_of_sales").asInt();
        return new HtmlCssTheme(title, url, imageUrl, downloads);
    }

    public static void main(String[] args) {
        List<HtmlCssTheme> themes = popularThemes();
        log.debug(Json.serializer().toPrettyString(themes));
    }
}
