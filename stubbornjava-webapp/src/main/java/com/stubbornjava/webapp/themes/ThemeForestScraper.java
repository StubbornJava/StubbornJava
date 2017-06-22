package com.stubbornjava.webapp.themes;

import java.util.List;
import java.util.Optional;

import org.jooq.lambda.Seq;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
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

        Elements elements = Jsoup.parse(html).select(".product-list__column-detail");
        List<HtmlCssTheme> themes = Seq.seq(elements).map(ThemeForestScraper::themeFromElement).toList();

        return themes;
    }

    private static HtmlCssTheme themeFromElement(Element element) {
        Element titleElement = element.select(".product-list__heading a").first();
        String title = titleElement.text();
        String url = HttpUrl.parse(HOST + titleElement.attr("href"))
                            .newBuilder()
                            .addQueryParameter("ref", affilaiteCode)
                            .build().toString();
        String imageUrl = element.select(".item-thumbnail__image img").attr("data-preview-url");
        int downloads = Optional.of(element.select(".product-list__info-sale").text())
                                .filter(val -> !Strings.isNullOrEmpty(val))
                                .map(val -> val.replace(" Sales", ""))
                                .map(Integer::parseInt)
                                .orElse(0);
        return new HtmlCssTheme(title, url, imageUrl, downloads);
    }

    public static void main(String[] args) {
        List<HtmlCssTheme> themes = popularThemes();
        log.debug(Json.serializer().toPrettyString(themes));
    }
}
