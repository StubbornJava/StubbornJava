package com.stubbornjava.common.seo;

import static com.stubbornjava.common.undertow.handlers.CustomHandlers.timed;

import com.stubbornjava.common.undertow.Exchange;

import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;

// {{start:SitemapRoutes}}
public class SitemapRoutes {
    private final InMemorySitemap sitemap;
    private SitemapRoutes(InMemorySitemap sitemap) {
        this.sitemap = sitemap;
    }

    public void getSitemap(HttpServerExchange exchange) {
        String sitemapName = Exchange.pathParams().pathParam(exchange, "sitemap").orElse(null);
        String content = sitemap.getIndex(sitemapName);
        if (null == content) {
            exchange.setStatusCode(404);
            Exchange.body().sendText(exchange, String.format("Sitemap %s doesn't exist", sitemapName));
            return;
        }
        Exchange.body().sendXml(exchange, content);
    }

    /*
     * Routing Handlers can be reused and combined with each other
     * using the RoutingHandler.addAll() method.
     */
    public static RoutingHandler router(InMemorySitemap sitemap) {
        SitemapRoutes routes = new SitemapRoutes(sitemap);
        RoutingHandler router = new RoutingHandler()
            .get("/sitemaps/{sitemap}", timed("getSitemap", routes::getSitemap))
        ;
        return router;
    }
}
// {{end:SitemapRoutes}}

