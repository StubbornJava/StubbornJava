package com.stubbornjava.webapp;

import static com.stubbornjava.common.undertow.handlers.CustomHandlers.timed;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stubbornjava.common.seo.SitemapRoutes;
import com.stubbornjava.common.undertow.SimpleServer;
import com.stubbornjava.common.undertow.handlers.CustomHandlers;
import com.stubbornjava.undertow.handlers.MiddlewareBuilder;
import com.stubbornjava.webapp.guide.GuideRoutes;
import com.stubbornjava.webapp.post.JavaLibRoutes;
import com.stubbornjava.webapp.post.PostRoutes;
import com.stubbornjava.webapp.themes.ThemeRoutes;

import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.server.handlers.PathHandler;

public class StubbornJavaWebApp {
    private static final Logger logger = LoggerFactory.getLogger(StubbornJavaWebApp.class);

    private static HttpHandler exceptionHandler(HttpHandler next) {
        return CustomHandlers.exception(next)
//         .addExceptionHandler(ResponseException.NotFound.class, HelperRoutes::notFound)
           .addExceptionHandler(Throwable.class, PageRoutes::error);
    }

    private static HttpHandler wrapWithMiddleware(HttpHandler handler) {
        return MiddlewareBuilder.begin(PageRoutes::redirector)
                                .next(BlockingHandler::new)
                                .next(CustomHandlers::gzip)
                                .next(ex -> CustomHandlers.accessLog(ex, logger))
                                .next(CustomHandlers::statusCodeMetrics)
                                .next(StubbornJavaWebApp::exceptionHandler)
                                .complete(handler);
    }

    // These routes do not require any authentication
    private static final HttpHandler basicRoutes = new RoutingHandler()
        .get("/", timed("getHome", PageRoutes::home))
        .get("/ping", timed("ping", PageRoutes::ping))

        .get("/favicon.ico", timed("favicon", CustomHandlers.resource("images/", (int)TimeUnit.DAYS.toSeconds(30))))
        .get("robots.txt", timed("robots", PageRoutes::robots))

        .get("/posts/{slug}", timed("getPost", PostRoutes::getPost))

        .get("/tags/{tag}/posts", timed("getRecentPostsWithTag", PostRoutes::recentPostsWithTag))

        .get("/java-libraries", timed("getJavaLibraries", JavaLibRoutes::listLibraries))
        .get("/java-libraries/{library}", timed("getLibrary", JavaLibRoutes::getLibrary))
        .get("/libraries/{library}/posts", timed("getLibraryRedirect", JavaLibRoutes::getLibraryRedirect))

        .get("/guides/{slug}", timed("getGuide", GuideRoutes::getGuide))

        .get("/best-selling-html-css-themes-and-website-templates", timed("popularThemes", ThemeRoutes::popularThemes))

        .get("/dev/metrics", timed("getMetrics", HelperRoutes::getMetrics))

        // addAll allows you to combine more than one RoutingHandler together.
        .addAll(SitemapRoutes.router(StubbornJavaSitemapGenerator.getSitemap()))

        .setFallbackHandler(timed("notFound", PageRoutes::notFound))
    ;

    private static final HttpHandler staticRoutes = new PathHandler(basicRoutes)
        .addPrefixPath("/assets", timed("getAssets", CustomHandlers.resource("", (int)TimeUnit.DAYS.toSeconds(30))))
    ;

    private static void startServer() {
        SimpleServer server = SimpleServer.simpleServer(wrapWithMiddleware(staticRoutes));
        server.start();
    }

    public static void main(String[] args) {
        startServer();
    }
}
