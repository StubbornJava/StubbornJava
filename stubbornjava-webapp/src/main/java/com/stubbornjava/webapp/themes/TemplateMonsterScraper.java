package com.stubbornjava.webapp.themes;

import java.util.List;

import org.jooq.lambda.Seq;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stubbornjava.common.HttpClient;
import com.stubbornjava.common.Json;
import com.stubbornjava.common.Retry;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class TemplateMonsterScraper {
    private static final Logger log = LoggerFactory.getLogger(TemplateMonsterScraper.class);
    private static final String affilaiteCode = "stubbornjava";
    private static final String HOST = "https://www.templatemonster.com";
    private static final String POPULAR_THEMES_URL =
        "https://www.templatemonster.com/best-selling-website-templates/";
    private static final OkHttpClient client = HttpClient.globalClient();

    public static List<HtmlCssTheme> popularThemes() {
        HttpUrl url = HttpUrl.parse(POPULAR_THEMES_URL);
        Request request = new Request.Builder().url(url).get().build();
        String html = Retry.retryUntilSuccessfulWithBackoff(
            () -> client.newCall(request).execute()
        );

        Elements elements = Jsoup.parse(html).select("#products .thumbnail");
        List<HtmlCssTheme> themes = Seq.seq(elements).map(TemplateMonsterScraper::themeFromElement).toList();

        return themes;
    }

    private static HtmlCssTheme themeFromElement(Element element) {
        String title = element.select(".small-prev-data h3").text();
        String url = HttpUrl.parse(element.select(".small-prev-data a").attr("href"))
                            .newBuilder()
                            .addQueryParameter("aff", affilaiteCode)
                            .build().toString();
        String imageUrl = element.select(".thumbnail-preview link").attr("href");

        /*
         *  Downloads aren't available on this page and we
         *  currently don't want to scrape all the detail pages
         *  just to get download counts.
         */
        int downloads = 0;
        return new HtmlCssTheme(title, url, imageUrl, downloads);
    }

    public static void main(String[] args) {
        List<HtmlCssTheme> themes = popularThemes();
        log.debug(Json.serializer().toPrettyString(themes));
    }
}
