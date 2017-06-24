package com.stubbornjava.webapp.themes;

import java.time.LocalDate;
import java.util.List;

import com.stubbornjava.common.undertow.Exchange;
import com.stubbornjava.webapp.Response;

import io.undertow.server.HttpServerExchange;

// {{start:routes}}
public class ThemeRoutes {

    public static void popularThemes(HttpServerExchange exchange) {
        List<HtmlCssTheme> themes = Themes.getPopularThemes(96);
        int year = LocalDate.now().getYear();
        Response response = Response.fromExchange(exchange)
            .with("year", year)
            .with("themes", themes)
            .withLibCounts()
            .withRecentPosts();
        Exchange.body().sendHtmlTemplate(exchange, "templates/src/pages/popular-themes", response);
    }
}
// {{end:routes}}
