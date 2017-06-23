package com.stubbornjava.webapp.guide;

import com.stubbornjava.common.undertow.Exchange;
import com.stubbornjava.webapp.PageRoutes;
import com.stubbornjava.webapp.Response;

import io.undertow.server.HttpServerExchange;

public class GuideRoutes {
    private GuideRoutes() {}

    public static void getGuide(HttpServerExchange exchange) {
        String slug = GuideParams.slug(exchange);
        Guide guide = Guides.findBySlug(slug);

        if (null == guide) {
            PageRoutes.notFound(exchange);
            return;
        }

        Response response = Response.fromExchange(exchange)
                .with("guide", guide)
                .withRecentPosts()
                .withLibCounts();
        Exchange.body().sendHtmlTemplate(exchange, "templates/src/pages/guide", response);
    }
}
