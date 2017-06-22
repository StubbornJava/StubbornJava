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

public class BootstrapBayScraper {
    private static final Logger log = LoggerFactory.getLogger(BootstrapBayScraper.class);
    private static final String affilaiteCode = "stubbornjava";
    private static final String HOST = "http://bootstrapbay.com";
    private static final String POPULAR_THEMES_URL =
        "http://bootstrapbay.com/themes?sort=sales";
    private static final OkHttpClient client = HttpClient.globalClient();

    public static List<HtmlCssTheme> popularThemes() {
        HttpUrl url = HttpUrl.parse(POPULAR_THEMES_URL);
        Request request = new Request.Builder().url(url).get().build();
        String html = Retry.retryUntilSuccessfulWithBackoff(
            () -> client.newCall(request).execute()
        );

        Elements elements = Jsoup.parse(html).select(".item");
        List<HtmlCssTheme> themes = Seq.seq(elements).map(BootstrapBayScraper::themeFromElement).toList();

        return themes;
    }

    private static HtmlCssTheme themeFromElement(Element element) {
        Element titleElement = element.select(".title a").first();
        String title = titleElement.text();
        String url = HttpUrl.parse(HOST + titleElement.attr("href"))
                            .newBuilder()
                            .addQueryParameter("ref", affilaiteCode)
                            .build().toString();
        String imageUrl = HOST + element.select(".imgWrap img").attr("src");
        int downloads = Optional.of(element.select(".sales").text())
                                .filter(val -> !Strings.isNullOrEmpty(val))
                                .map(val -> val.replace(" sales", ""))
                                .map(Integer::parseInt)
                                .orElse(0);
        return new HtmlCssTheme(title, url, imageUrl, downloads);
    }

    public static void main(String[] args) {
        List<HtmlCssTheme> themes = popularThemes();
        log.debug(Json.serializer().toPrettyString(themes));
    }
}
